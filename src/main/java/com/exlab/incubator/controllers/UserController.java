package com.exlab.incubator.controllers;

import com.exlab.incubator.entities.User;
import com.exlab.incubator.services.service_interfaces.UserService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/list")
    public List<User> getAllUsers(){
        return userService.getAllUsers();
    }

}
