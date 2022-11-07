package com.exlab.incubator.dto.requests;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequest {

    private String username;
    private String password;
    private String email;
    private String phoneNumber;

    @Override
    public String toString() {
        return "SignupRequest{" +
            "username='" + username + '\'' +
            ", password='" + password + '\'' +
            ", email='" + email + '\'' +
            ", phoneNumber='" + phoneNumber + '\'' +
            '}';
    }
}