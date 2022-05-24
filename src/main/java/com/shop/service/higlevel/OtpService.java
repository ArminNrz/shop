package com.shop.service.higlevel;

import com.shop.common.Constant;
import com.shop.dto.auth.ChangePasswordDTO;
import com.shop.dto.auth.OtpResponse;
import com.shop.entity.AppUser;
import com.shop.entity.OtpEntity;
import com.shop.entity.Role;
import com.shop.repository.OtpRepository;
import com.shop.service.entity.UserService;
import com.shop.service.lowlevel.SecurityService;
import com.shop.utility.RandomCodeGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class OtpService {

    private final SecurityService securityService;
    private final UserService userService;
    private final OtpRepository otpRepository;
    private final RandomCodeGenerator randomCodeGenerator;

    public void send(String phoneNumber) {
        log.debug("Request to send otp for login to user with phone number: {}", phoneNumber);
        OtpEntity otpEntity = getOtpEntity(phoneNumber);
        otpRepository.addLoginOtp(otpEntity);
        //todo: send with sms to customer phone
    }

    public void forgetPassword(String phoneNumber) {
        log.debug("Request to send otp for forget password to user with phone number: {}", phoneNumber);
        OtpEntity otp = getOtpEntity(phoneNumber);
        otpRepository.addForgetPasswordOtp(otp);
        //todo: send with sms to customer phone
    }

    private OtpEntity getOtpEntity(String phoneNumber) {
        /*
        find user
         */
        AppUser user = userService.getByPhoneNumber(phoneNumber);
        if (user == null) {
            log.error("No such user exist with phone number: {}", phoneNumber);
            throw Problem.valueOf(Status.NOT_FOUND, Constant.APP_USER_NOT_FOUND);
        }

        /*
        generate random code
         */
        String code = randomCodeGenerator.generate();

        OtpEntity otpEntity = new OtpEntity();
        otpEntity.setPhoneNumber(phoneNumber);
        otpEntity.setOtpCode(code);
        return otpEntity;
    }

    public OtpResponse login(OtpEntity otpEntity, HttpServletRequest request) {

        String phoneNumber = otpEntity.getPhoneNumber();

        OtpEntity otp = otpRepository.findByPhoneNumberOtpLogin(phoneNumber);
        if (otp == null || !otp.getOtpCode().equals(otpEntity.getOtpCode())) {
            log.error("Otp code is not correct");
            throw Problem.valueOf(Status.UNAUTHORIZED, Constant.APP_USER_OTP_WRONG);
        }

        AppUser appUser = userService.getByPhoneNumber(phoneNumber);
        Collection<Role> roles = appUser.getRoles();
        List<String> rolesString = roles.stream().map(Role::getName).collect(Collectors.toList());

        String accessToken = securityService.createAccessToken(request, otpEntity.getPhoneNumber(), rolesString);
        OtpResponse response = new OtpResponse();
        response.setAccessToken(accessToken);

        return response;
    }

    public void changePassword(ChangePasswordDTO changePasswordDTO) {
        log.debug("Request to change password for phoneNumber: {}", changePasswordDTO.getPhoneNumber());

        String phoneNumber = changePasswordDTO.getPhoneNumber();
        OtpEntity otp = otpRepository.findByPhoneNumberOtpForgetPassword(phoneNumber);
        if (otp == null || !otp.getOtpCode().equals(changePasswordDTO.getOtpCode())) {
            log.error("Otp code is not correct");
            throw Problem.valueOf(Status.UNAUTHORIZED, Constant.APP_USER_OTP_WRONG);
        }

        userService.changePassword(phoneNumber, changePasswordDTO.getNewPassword());
    }
}
