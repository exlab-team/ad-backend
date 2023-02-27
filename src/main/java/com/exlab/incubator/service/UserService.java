package com.exlab.incubator.service;

import com.exlab.incubator.dto.requests.UserCreateDto;
import com.exlab.incubator.dto.requests.UserLoginDto;
import com.exlab.incubator.dto.responses.UserAccountReadDto;
import com.exlab.incubator.entity.Role;

public interface UserService {

    UserAccountReadDto loginUser(UserLoginDto userLoginDto);
    Long createUser(UserCreateDto userCreateDto, Role role);
    boolean deleteUserById(long id);

}
