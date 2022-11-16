package com.exlab.incubator.dto.requests;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
public class SignupRequest {

    /*
        В поле username и password первый и последний знаки, который не должны повторяться.
    */

    @NotBlank
    @Pattern(regexp = "[0-9a-zA-Z@/_.]{5,30}", message = "Username can contains only letters, numbers and symbols: { @ / _ . }")
    @Size(min = 5, max = 30, message = "Username size is not correct")
    private String username;

    @NotBlank
    @Size(min = 16, max = 16, message = "Password size is not correct")
    @Pattern(regexp = "[0-9a-zA-Zа-яА-Я]{16}", message = "Password can contains only letters and numbers")
    private String password;

    @NotBlank
    @Email
    private String email;
}