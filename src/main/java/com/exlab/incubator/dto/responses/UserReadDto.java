package com.exlab.incubator.dto.responses;

import java.time.Instant;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class UserReadDto {

    long id;
    String username;
    String email;
    Instant createdAt;
    long userAccountId;
}
