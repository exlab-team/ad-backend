package com.exlab.incubator.services.interfaces;

import com.exlab.incubator.dto.requests.LoginRequest;
import com.exlab.incubator.dto.requests.SignupRequest;
import com.exlab.incubator.entities.User;
import org.springframework.http.ResponseEntity;

public interface UserService {
    ResponseEntity<?> authUser(LoginRequest loginRequest);

    ResponseEntity<?> registerUser(SignupRequest signupRequest);

    User getUserByUsername(String username);
}
