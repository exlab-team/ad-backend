package com.exlab.incubator.dto.requests;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class LoginRequest {

    @NotBlank
    @Size(min = 5, max = 30, message = "Username is not correct")
    private String username;

    @NotBlank
    @Size(min = 16, max = 16, message = "Password is not correct")
    private String password;
}
