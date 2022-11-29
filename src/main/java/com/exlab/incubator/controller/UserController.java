package com.exlab.incubator.controller;

import com.exlab.incubator.dto.requests.UserCreateDto;
import com.exlab.incubator.dto.requests.UserLoginDto;
import com.exlab.incubator.service.UserService;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/user")
    public ResponseEntity<?> loginUser(@Valid @RequestBody UserLoginDto userLoginDto) {
        return userService.authUser(userLoginDto);
    }

    @PostMapping()
    public ResponseEntity<?> createUser(@Valid @RequestBody UserCreateDto userCreateDto) {
        return userService.registerUser(userCreateDto);
    }

    @PostMapping("/user/{email}")
    public ResponseEntity<?> resendingTheVerificationLink(@PathVariable String email) {
        return userService.resendingTheLinkToTheEmail(email);
    }

    @GetMapping("/user/{code}")
    public String activateUserByTheActivationCodeFromTheLink(@PathVariable String code) {
        return userService.activateUserByCode(code);
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable int id){
        return userService.deleteUserById(id);
    }
}
