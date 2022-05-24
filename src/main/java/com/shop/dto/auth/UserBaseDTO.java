package com.shop.dto.auth;

import com.shop.common.Constant;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Data
public class UserBaseDTO implements Serializable {

    @NotEmpty(message = Constant.APP_USER_FIRST_NAME_EMPTY)
    private String firstName;

    @NotEmpty(message = Constant.APP_USER_LAST_NAME_EMPTY)
    private String lastName;

    @NotEmpty(message = Constant.APP_USER_PHONE_NUMBER_EMPTY)
    private String phoneNumber;
}
