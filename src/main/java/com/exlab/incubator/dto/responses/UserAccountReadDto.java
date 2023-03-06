package com.exlab.incubator.dto.responses;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class UserAccountReadDto {

    String token;
    String type = "Bearer";
    long userId;
    long accountId;
    String username;
    String email;
    String personalAccount;

}
