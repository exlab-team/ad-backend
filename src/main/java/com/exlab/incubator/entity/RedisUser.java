package com.exlab.incubator.entity;

import java.io.Serializable;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@RedisHash("IncubatorUser")
public class RedisUser implements Serializable {

    private String username;

    private String password;

    private String email;

    private boolean emailVerified;

    private Instant createdAt;

    private Instant timeOfSendingVerificationLink;

    private String activationCode;
}

