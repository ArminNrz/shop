package com.shop.service.entity;

import com.shop.common.Constant;
import com.shop.dto.auth.UserCreateDTO;
import com.shop.dto.auth.UserDTO;
import com.shop.entity.AppUser;
import com.shop.entity.Role;
import com.shop.mapper.UserMapper;
import com.shop.repository.AppUserRepository;
import com.shop.specification.AppUserSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService implements UserDetailsService {

    private final AppUserRepository repository;
    private final UserMapper mapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;

    @Override
    public UserDetails loadUserByUsername(String phoneNumber) throws UsernameNotFoundException {

        AppUser user = this.getByPhoneNumber(phoneNumber);

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getName())));

        return new User(user.getPhoneNumber(), user.getPassword(), authorities);
    }

    public AppUser getByPhoneNumber(String phoneNumber) {
        log.debug("Try to find User by phone number: {}", phoneNumber);

        Optional<AppUser> appUserOptional = repository.findByPhoneNumber(phoneNumber);
        if (appUserOptional.isEmpty()) {
            log.warn("No such User exist with phone number: {}", phoneNumber);
            return null;
        }

        log.debug("Found user: {}", appUserOptional.get());
        return appUserOptional.get();
    }

    public UserDTO create(UserCreateDTO createDTO) {
        log.debug("Try to create User: {}", createDTO);

        AppUser user = mapper.toEntity(createDTO);

        user.setPassword(passwordEncoder.encode(createDTO.getPassword()));
        grantUserRole(user);

        user = repository.save(user);
        log.info("Saved user: {}", user);

        return mapper.toDto(user);
    }

    public void changePassword(String phoneNumber, String newPassword) {
        log.debug("Request to change password for user with phone number: {}", phoneNumber);

        AppUser entity = this.getByPhoneNumber(phoneNumber);
        if (entity == null) {
            log.error("No such user exist with phone number: {}", phoneNumber);
            throw Problem.valueOf(Status.NOT_FOUND, Constant.APP_USER_NOT_FOUND);
        }

        entity.setPassword(passwordEncoder.encode(newPassword));
        repository.save(entity);
        log.info("Changed user with phone number: {}, password", phoneNumber);
    }

    private void grantUserRole(AppUser user) {
        Role userRole = roleService.get("ROLE_USER");
        user.getRoles().add(userRole);
        log.debug("Grant role user to user: {}", user);
    }

    public Page<UserDTO> getAll(AppUserSpecification specification, Pageable pageable) {
        return repository.findAll(specification, pageable)
                .map(mapper::toDto);
    }
}
