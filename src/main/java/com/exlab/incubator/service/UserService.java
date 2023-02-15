package com.exlab.incubator.service;

import com.exlab.incubator.dto.requests.UserCreateDto;
import com.exlab.incubator.dto.requests.UserLoginDto;
import com.exlab.incubator.dto.responses.UserDto;
import com.exlab.incubator.entity.Role;

public interface UserService {

    UserDto loginUser(UserLoginDto userLoginDto);

    Long createUser(UserCreateDto userCreateDto, Role role);

    boolean deleteUserById(long id);

    boolean activateUserByCode(String activationCode);
}
