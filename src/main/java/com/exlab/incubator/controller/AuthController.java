package com.exlab.incubator.controller;

import static org.springframework.http.ResponseEntity.ok;

import com.exlab.incubator.dto.requests.UserLoginDto;
import com.exlab.incubator.dto.responses.UserAccountReadDto;
import com.exlab.incubator.dto.responses.UserReadDto;
import com.exlab.incubator.service.UserAccountService;
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
    private final UserAccountService userAccountService;

    @Autowired
    public AuthController(UserService userService, UserAccountService userAccountService) {
        this.userService = userService;
        this.userAccountService = userAccountService;
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

    @Operation(summary = "Activate email by code and create account")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successful activation",
            content = {
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = UserAccountReadDto.class))
            }),
        @ApiResponse(responseCode = "404", description = "Activation code is invalid",
            content = @Content),
        @ApiResponse(responseCode = "400", description = "Account has already activated",
            content = @Content)})
    @GetMapping("/{activationCode}")
    public ResponseEntity<Long> activateUserAccount(@PathVariable
    @Parameter(description = "Activation code from email link") String activationCode) {
        return ok(userAccountService.activateUserAccountByCode(activationCode));
    }
}
