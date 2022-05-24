package com.shop.dto.auth;

import com.shop.common.Constant;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotEmpty;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserCreateDTO extends UserBaseDTO {

    @NotEmpty(message = Constant.APP_USER_PASSWORD_EMPTY)
    private String password;

    private String superUserPhoneNumber;
}
