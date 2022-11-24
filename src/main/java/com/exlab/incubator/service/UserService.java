package com.exlab.incubator.service;

import com.exlab.incubator.dto.requests.LoginRequest;
import com.exlab.incubator.dto.requests.SignupRequest;
import org.springframework.http.ResponseEntity;

public interface UserService {
    ResponseEntity<?> authUser(LoginRequest loginRequest);

    ResponseEntity<?> registerUser(SignupRequest signupRequest);

    ResponseEntity<?> deleteUserById(int id);

    String activateUserByCode(String code);

    ResponseEntity<?> resendingTheLinkToTheEmail(String email);
}
