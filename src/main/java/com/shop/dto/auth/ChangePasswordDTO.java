package com.shop.dto.auth;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.shop.common.Constant;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ChangePasswordDTO {
    @JsonIgnore
    private String phoneNumber;
    @NotNull(message = Constant.APP_USER_OTP_EMPTY)
    private String otpCode;
    @NotNull(message = Constant.APP_USER_NEW_PASSWORD_EMPTY)
    private String newPassword;
}
