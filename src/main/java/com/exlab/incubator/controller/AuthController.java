package com.exlab.incubator.controller;

import static org.springframework.http.ResponseEntity.badRequest;
import static org.springframework.http.ResponseEntity.ok;

import com.exlab.incubator.dto.requests.UserLoginDto;
import com.exlab.incubator.dto.responses.UserDto;
import com.exlab.incubator.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Operation(summary = "User login")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Login user",
            content = {
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = UserDto.class))
            }),
        @ApiResponse(responseCode = "406", description = "Email doesn't verified",
            content = @Content),
        @ApiResponse(responseCode = "401", description = "If incorrect login or password.",
            content = @Content)})
    @PostMapping("/login")
    public ResponseEntity<UserDto> loginUser(@Valid @RequestBody UserLoginDto userLoginDto) {
        UserDto userDto = userService.loginUser(userLoginDto);
        return ok(userDto);

    }

    @Operation(summary = "Activate email by code")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successful activation"),
        @ApiResponse(responseCode = "404", description = "Activation code is invalid",
            content = @Content),
        @ApiResponse(responseCode = "400", description = "Email already verified",
            content = @Content)})
    @GetMapping("/{activationCode}")
    public ResponseEntity<?> activateUserAccount(@PathVariable
    @Parameter(description = "Activation code from email link") String activationCode) {
        return userService.activateUserByCode(activationCode)
            ? ok().build()
            : badRequest().body("Email already verified");
    }
}
