package com.exlab.incubator.service.impl;

import com.exlab.incubator.entity.Role;
import com.exlab.incubator.entity.RoleName;
import com.exlab.incubator.entity.User;
import com.exlab.incubator.repository.RoleRepository;
import com.exlab.incubator.service.RoleService;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository repository;

    @Autowired
    public RoleServiceImpl(RoleRepository repository) {
        this.repository = repository;
    }

    @Override
    public Role createRoleIfNotExist(RoleName roleName) {
        Optional<Role> optRole = repository.findByRoleName(roleName);

        return repository.findByRoleName(roleName).orElseGet(
            () -> repository.save(
                Role.builder()
                    .roleName(roleName)
                    .build()));
    }

}
