package com.exlab.incubator.service;

import com.exlab.incubator.entity.Role;
import com.exlab.incubator.entity.RoleName;

public interface RoleService {

    Role createRoleIfNotExist(RoleName roleName);
}
