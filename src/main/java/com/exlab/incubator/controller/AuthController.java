package com.exlab.incubator.controller;

import com.exlab.incubator.dto.requests.UserLoginDto;
import com.exlab.incubator.dto.responses.UserDto;
import com.exlab.incubator.service.UserService;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/auth")
public class AuthController {

    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<UserDto> loginUser(@Valid @RequestBody UserLoginDto userLoginDto) {
        UserDto userDto = userService.loginUser(userLoginDto);
        return userDto.isEmailVerified()
            ? ResponseEntity.ok(userDto)
            : ResponseEntity.badRequest().build();

           /* ? new ResponseEntity<>(userDto, HttpStatus.OK)
            : new ResponseEntity<>(HttpStatus.BAD_REQUEST); // добавила проверку*/
    }

    @GetMapping("/{activationCode}")
    public ResponseEntity<String> activateUserAccount(@PathVariable String activationCode) {
        return new ResponseEntity<>(userService.activateUserByCode(activationCode), HttpStatus.OK);
    }
}
