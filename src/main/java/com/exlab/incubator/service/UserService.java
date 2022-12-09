package com.exlab.incubator.service;

import com.exlab.incubator.dto.requests.UserLoginDto;
import com.exlab.incubator.dto.requests.UserCreateDto;
import com.exlab.incubator.dto.responses.MessageDto;
import com.exlab.incubator.dto.responses.UserDto;
import org.springframework.http.ResponseEntity;

public interface UserService {
    UserDto loginUser(UserLoginDto userLoginDto);

    ResponseEntity<MessageDto> createUser(UserCreateDto userCreateDto);

    String deleteUserById(int id);

    String activateUserByCode(String code);

    ResponseEntity<MessageDto> resendingTheVerificationLink(String email);
}
