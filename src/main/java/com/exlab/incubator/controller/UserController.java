package com.exlab.incubator.controller;

import com.exlab.incubator.dto.requests.UserCreateDto;
import com.exlab.incubator.dto.requests.UserLoginDto;
import com.exlab.incubator.dto.responses.MessageDto;
import com.exlab.incubator.dto.responses.UserDto;
import com.exlab.incubator.service.UserService;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<MessageDto> createUser(@Valid @RequestBody UserCreateDto userCreateDto) {
        return new ResponseEntity<>(userService.createUser(userCreateDto), HttpStatus.CREATED); //исправила статус, изменить возвращаемое значение
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable int id) {
        return new ResponseEntity<>(userService.deleteUserById(id), HttpStatus.NO_CONTENT); //исправила статус, изменить возвращаемое значение
    }
}
