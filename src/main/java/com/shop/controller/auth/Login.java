package com.shop.controller.auth;

import com.shop.common.Constant;
import com.shop.dto.auth.OtpResponse;
import com.shop.entity.OtpEntity;
import com.shop.service.higlevel.OtpService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(Constant.BASE_URL + "/login/otp")
public class Login {

    private final OtpService otpService;

    @GetMapping
    public ResponseEntity<Void> getOTPCode(@RequestParam("phoneNumber") String phoneNumber) {
        log.info("REST request to get OTP code for User with phone number: {}", phoneNumber);
        otpService.send(phoneNumber);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{phoneNumber}")
    public ResponseEntity<OtpResponse> login(@PathVariable("phoneNumber") String phoneNumber, @Valid @RequestBody OtpEntity otpEntity, HttpServletRequest request) {
        log.info("REST request to login with otp for phoneNumber: {}", phoneNumber);
        otpEntity.setPhoneNumber(phoneNumber);
        OtpResponse response = otpService.login(otpEntity, request);
        return ResponseEntity.ok().body(response);
    }
}
