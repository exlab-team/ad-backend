package com.exlab.incubator.service;

import com.exlab.incubator.dto.requests.UserLoginDto;
import com.exlab.incubator.dto.requests.UserCreateDto;
import com.exlab.incubator.dto.responses.MessageDto;
import com.exlab.incubator.dto.responses.UserDto;

public interface UserService {

    UserDto loginUser(UserLoginDto userLoginDto);

    Long createUser(UserCreateDto userCreateDto);

    boolean deleteUserById(long id);

    boolean activateUserByCode(String activationCode);
}
