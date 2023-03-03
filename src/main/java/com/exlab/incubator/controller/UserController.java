package com.exlab.incubator.controller;

import static org.springframework.http.ResponseEntity.noContent;
import static org.springframework.http.ResponseEntity.notFound;

import com.exlab.incubator.dto.requests.UserCreateDto;
import com.exlab.incubator.entity.Role;
import com.exlab.incubator.entity.RoleName;
import com.exlab.incubator.service.RoleService;
import com.exlab.incubator.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
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
    public UserController(UserService userService, RoleService roleService) {
        this.userService = userService;
    }

    @Operation(summary = "Delete user", description = "Delete user from database")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "No content if user was deleted from database",
            content = @Content),
        @ApiResponse(responseCode = "404", description = "Not found if user wasn't found in database",
            content = @Content),
        @ApiResponse(responseCode = "401", description = "if user wasn't authorized",
            content = @Content)})
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(
        @PathVariable @Parameter(description = "user's id") long id) {
        return userService.deleteUserById(id)
            ? noContent().build()
            : notFound().build();
    }
}
