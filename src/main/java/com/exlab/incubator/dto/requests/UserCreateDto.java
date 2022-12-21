package com.exlab.incubator.dto.requests;

import com.exlab.incubator.validation.EmailConstraint;
import com.exlab.incubator.validation.PasswordEquality;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Value;

@Value
@PasswordEquality(field = "password", equalsTo = "confirmPassword")
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
    @Email(regexp = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
            + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$", message = "It allows numeric values from 0 to 9.\n" +
            "Both uppercase and lowercase letters from a to z are allowed.\n" +
            "Allowed are underscore “_”, hyphen “-“, and dot “.”\n" +
            "Dot isn't allowed at the start and end of the local part.\n" +
            "Consecutive dots aren't allowed.\n" +
            "For the local part, a maximum of 64 characters are allowed.\n" +
            "It allows numeric values from 0 to 9.\n" +
            "We allow both uppercase and lowercase letters from a to z.\n" +
            "Hyphen “-” and dot “.” aren't allowed at the start and end of the domain part.\n" +
            "No consecutive dots.")
    private String email;


//    @NotBlank
//    @Size(max = 256, message = "Email max size is 256 symbols")
//    @EmailConstraint
//    private String email;

}