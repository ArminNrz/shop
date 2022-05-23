package com.shop.service.entity;

import com.shop.entity.Role;
import com.shop.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository repository;

    public Role saveOrUpdate(Role role) {
        log.debug("Try to save, role: {}", role);
        role = repository.save(role);
        log.info("Saved role: {}", role);
        return role;
    }

    public Role get(String name) {

        Optional<Role> roleOptional = repository.findByName(name);

        if (roleOptional.isEmpty())
            throw Problem.valueOf(Status.BAD_REQUEST, "Role with this name not exist");

        Role foundRole = roleOptional.get();
        log.debug("Role with name: {}, is: {}", name, foundRole);

        return foundRole;
    }
}
