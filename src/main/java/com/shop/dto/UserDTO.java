package com.shop.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserDTO extends UserBaseDTO {
    private Long id;
    private String superUserPhoneNumber;
}
