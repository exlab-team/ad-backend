package com.exlab.incubator.service;

import com.exlab.incubator.dto.requests.UserCreateDto;

public interface RedisService {

    Long registerUser(UserCreateDto userCreateDto);

    boolean activateUser(String email, String activationCode);
}