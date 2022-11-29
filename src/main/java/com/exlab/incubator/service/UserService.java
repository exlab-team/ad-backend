package com.exlab.incubator.service;

import com.exlab.incubator.dto.requests.UserLoginDto;
import com.exlab.incubator.dto.requests.UserCreateDto;
import org.springframework.http.ResponseEntity;

public interface UserService {
    ResponseEntity<?> authUser(UserLoginDto userLoginDto);

    ResponseEntity<?> registerUser(UserCreateDto userCreateDto);

    ResponseEntity<?> deleteUserById(int id);

    String activateUserByCode(String code);

    ResponseEntity<?> resendingTheLinkToTheEmail(String email);
}
