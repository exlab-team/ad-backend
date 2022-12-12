package com.exlab.incubator.controller;

import com.exlab.incubator.dto.requests.UserCreateDto;
import com.exlab.incubator.dto.requests.UserLoginDto;
import com.exlab.incubator.dto.responses.MessageDto;
import com.exlab.incubator.dto.responses.UserDto;
import com.exlab.incubator.service.UserService;
import exception.EmailExistsException;
import exception.UsernameExistsException;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
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

    @GetMapping
    public ResponseEntity<UserDto> loginUser(@Valid @RequestBody UserLoginDto userLoginDto) {
        return new ResponseEntity<>(userService.loginUser(userLoginDto), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<MessageDto> createUser(@Valid @RequestBody UserCreateDto userCreateDto) {
        return new ResponseEntity<>(userService.createUser(userCreateDto), HttpStatus.OK);
    }

    @PostMapping("/{email}")
    public ResponseEntity<MessageDto> resendingTheVerificationLink(@PathVariable String email) {
        return new ResponseEntity<>(userService.resendingTheVerificationLink(email), HttpStatus.OK);
    }

    @GetMapping("/{code}")
    public ResponseEntity<String> activateUserByTheActivationCodeFromTheLink(@PathVariable String code) {
        return new ResponseEntity<>(userService.activateUserByCode(code), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable int id){
        return new ResponseEntity<>(userService.deleteUserById(id), HttpStatus.OK);
    }

    @ExceptionHandler({ UsernameExistsException.class})
    public ResponseEntity<Object> handleException1() {
        return new ResponseEntity<>("Error: Username already exists", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ EmailExistsException.class})
    public ResponseEntity<Object> handleException2() {
        return new ResponseEntity<>("Error: Email already exists", HttpStatus.BAD_REQUEST);
    }
}
