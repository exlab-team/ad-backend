package com.exlab.incubator.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("/public")
    public String testPublicApi(){
        return "Public api";
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/user")
    public String testUserApi(){
        return "User api";
    }

}