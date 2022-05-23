package com.shop.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.shop.common.Constant;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Data
public class OtpEntity implements Serializable {
    @JsonIgnore
    private String phoneNumber;
    @NotEmpty(message = Constant.APP_USER_OTP_EMPTY)
    private String otpCode;
}
