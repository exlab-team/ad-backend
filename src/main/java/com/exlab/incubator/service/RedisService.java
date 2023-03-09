package com.exlab.incubator.service;

import com.exlab.incubator.dto.requests.UserCreateDto;

public interface RedisService {

    String registerUser(UserCreateDto userCreateDto);

    void verifyUser(String email, String activationCode);
}