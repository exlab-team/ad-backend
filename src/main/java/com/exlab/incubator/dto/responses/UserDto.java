package com.exlab.incubator.dto.responses;

import lombok.Value;

@Value
public class UserDto {

    private String token;
    private String type = "Bearer";
    private int id;
    private String username;
    private String email;


    public UserDto(String token, int id, String username, String email) {
        this.token = token;
        this.id = id;
        this.username = username;
        this.email = email;
    }
}
