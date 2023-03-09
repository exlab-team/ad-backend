package com.exlab.incubator.controller;

import static org.springframework.http.ResponseEntity.ok;
import com.exlab.incubator.dto.requests.UserCreateDto;
import com.exlab.incubator.dto.requests.UserLoginDto;
import com.exlab.incubator.dto.responses.UserAccountReadDto;
import com.exlab.incubator.service.RedisService;
import com.exlab.incubator.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/auth")
public class AuthController {

    private final UserService userService;
    private final RedisService redisService;

    @Autowired
    public AuthController(UserService userService, RedisService redisService) {
        this.userService = userService;
        this.redisService = redisService;
    }


    @Operation(summary = "User login")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Login user",
            content = {
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = UserAccountReadDto.class))
            }),
        @ApiResponse(responseCode = "406", description = "Email doesn't verified",
            content = @Content),
        @ApiResponse(responseCode = "401", description = "If incorrect login or password.",
            content = @Content)})
    @PostMapping("/login")
    public ResponseEntity<UserAccountReadDto> loginUser(@Valid @RequestBody UserLoginDto userLoginDto) {
        UserAccountReadDto userAccountReadDto = userService.loginUser(userLoginDto);
        return ok(userAccountReadDto);

    }

    @Operation(summary = "User registry", description = "Create user and save in Redis database. The user's activation code is returned")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Create user",
            content = @Content),
        @ApiResponse(responseCode = "400", description = "Error: Username already exists or Error: Email already exists or any validation errors",
            content = @Content)})
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@Valid @RequestBody UserCreateDto userCreateDto) {
        return new ResponseEntity<>(redisService.registerUser(userCreateDto),HttpStatus.CREATED);
    }


    @Operation(summary = "Verify user email")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successful activation"),
        @ApiResponse(responseCode = "404", description = "Activation link is outdated",
            content = @Content)})
    @GetMapping("/{activationCode}")
    public ResponseEntity<?> verifyUser(@PathVariable
    @Parameter(description = "Activation code from the email link") String activationCode, @RequestParam String email) {
        redisService.verifyUser(email, activationCode);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
