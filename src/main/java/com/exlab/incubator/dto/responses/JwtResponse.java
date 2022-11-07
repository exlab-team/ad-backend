package com.exlab.incubator.dto.responses;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtResponse {

    private String token;
    private String type = "Bearer";
    private int id;
    private String username;
    private String email;
    private String phoneNumber;


    public JwtResponse(String token, int id, String username, String email,
        String phoneNumber) {
        this.token = token;
        this.id = id;
        this.username = username;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }
}
