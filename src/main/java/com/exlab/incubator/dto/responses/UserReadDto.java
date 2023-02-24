package com.exlab.incubator.dto.responses;

import lombok.Value;

@Value
public class UserReadDto {

    String token;
    String type = "Bearer";
    long id;
    String username;
    String email;

}
