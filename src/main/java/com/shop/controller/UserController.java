package com.shop.controller;

import com.shop.common.Constant;
import com.shop.dto.ChangePasswordDTO;
import com.shop.dto.UserCreateDTO;
import com.shop.dto.UserDTO;
import com.shop.service.entity.UserService;
import com.shop.service.higlevel.OtpService;
import com.shop.specification.AppUserSpecification;
import com.shop.utility.PaginationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(Constant.BASE_URL + Constant.VERSION + "/users")
public class UserController {

    private final UserService service;
    private final OtpService otpService;

    @PostMapping("")
    public ResponseEntity<UserDTO> create(@Valid @RequestBody UserCreateDTO createDTO) {
        log.info("REST request to create User: {}", createDTO);
        UserDTO result = service.create(createDTO);
        return ResponseEntity.created(URI.create("/users")).body(result);
    }

    @PutMapping("/forget-password/{phoneNumber}")
    public ResponseEntity<Void> forgetPassword(@PathVariable("phoneNumber") String phoneNumber) {
        log.info("Rest request to forget password for phoneNumber: {}", phoneNumber);
        otpService.forgetPassword(phoneNumber);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/change-password/{phoneNumber}")
    public ResponseEntity<Void> changePassword(@PathVariable("phoneNumber") String phoneNumber, @Valid @RequestBody ChangePasswordDTO changePasswordDTO) {
        log.info("REST request to change password for phoneNumber: {}", phoneNumber);
        changePasswordDTO.setPhoneNumber(phoneNumber);
        otpService.changePassword(changePasswordDTO);
        return ResponseEntity.ok().body(null);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN')")
    public ResponseEntity<Page<UserDTO>> getAll(
            AppUserSpecification specification,
            @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        log.info("REST request to getAll Users, specification: {}", specification);
        Page<UserDTO> page = service.getAll(specification, pageable);
        return ResponseEntity.ok()
                .headers(PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page))
                .body(page);
    }

}
