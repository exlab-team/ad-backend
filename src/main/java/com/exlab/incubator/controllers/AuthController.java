package com.exlab.incubator.controllers;


import com.exlab.incubator.dto.requests.LoginRequest;
import com.exlab.incubator.dto.requests.SignupRequest;
import com.exlab.incubator.services.interfaces.UserService;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/authenticate")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {

    private UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authUser(@Valid @RequestBody LoginRequest loginRequest) {
        return userService.authUser(loginRequest);
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signupRequest) {
        return userService.registerUser(signupRequest);
    }

    @GetMapping("/activate/{code}")
    public String activate(@PathVariable String code) {
        System.out.println(code);

        int charAt = code.indexOf(".");
        System.out.println(charAt);
        String firstPart = code.substring(0, charAt);
        String secondPart = code.substring(charAt + 1);

        System.out.println(firstPart);
        System.out.println(secondPart);


        return userService.activateUserByCode(firstPart, secondPart);
    }
}
