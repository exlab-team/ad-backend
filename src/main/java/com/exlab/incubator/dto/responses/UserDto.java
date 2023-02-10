package com.exlab.incubator.dto.responses;

import lombok.Value;

@Value
public class UserDto {

    private String token;
    private String type = "Bearer";
    private long id;
    private String username;
    private String email;

}
