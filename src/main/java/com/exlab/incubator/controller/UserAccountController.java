package com.exlab.incubator.controller;

import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.http.ResponseEntity.status;

import com.exlab.incubator.dto.requests.TariffDto;
import com.exlab.incubator.entity.Tariff;
import com.exlab.incubator.service.TariffService;
import com.exlab.incubator.service.UserAccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/accounts")
public class UserAccountController {

    private final UserAccountService userAccountService;
    private final TariffService tariffService;

    @Autowired
    public UserAccountController(UserAccountService userAccountService,
        TariffService tariffService) {
        this.userAccountService = userAccountService;
        this.tariffService = tariffService;
    }

    @Operation(summary = "Set tariff plane to user account")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successful update"),
        @ApiResponse(responseCode = "404", description = "User account doesn't exist",
            content = @Content),
        @ApiResponse(responseCode = "400", description = "No such tariff name",
            content = @Content),
        @ApiResponse(responseCode = "304", description = "User account wasn't updated",
            content = @Content)})
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasRole('USER')")
    @PutMapping("/{id}/tariffs")
    public ResponseEntity<?> updateUserAccountWithTariff(@PathVariable Long id,
        @RequestBody TariffDto tariffDto) {
        Tariff tariff = tariffService.createTariffIfNotExist(tariffDto);
        return userAccountService.establishTariffToUser(id, tariff)
            ? ok().build()
            : status(HttpStatus.NOT_MODIFIED).build();
    }

}
