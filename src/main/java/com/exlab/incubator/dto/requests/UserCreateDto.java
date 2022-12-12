package com.exlab.incubator.dto.requests;

import com.exlab.incubator.validation.PasswordEquality;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.Value;

@Value
@PasswordEquality( field="password", equalsTo="confirmPassword" )
public class UserCreateDto {

    @NotBlank
    @Size(min = 5, max = 30, message = "Username min size is 5 symbols and max size is 30 symbols")
    @Pattern(regexp = "[a-zA-Z<>'-]{5,30}", message = "Username can contains only letters and symbols: { < > - }")
    private String username;

    @NotBlank
    @Size(min = 16, max = 16, message = "Password size is 16 symbols")
    @Pattern(regexp = "[0-9a-zA-Zа-яА-Я]{16}", message = "Password can contains only letters and numbers")
    private String password;

    @NotBlank
    @Size(min = 16, max = 16, message = "Password size is 16 symbols")
    @Pattern(regexp = "[0-9a-zA-Zа-яА-Я]{16}", message = "Password can contains only letters and numbers")
    private String confirmPassword;


    @NotBlank
    @Size(max = 256, message = "Email max size is 256 symbols")
    @Email
    private String email;
}