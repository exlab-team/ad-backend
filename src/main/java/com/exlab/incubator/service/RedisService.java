package com.exlab.incubator.service;

import com.exlab.incubator.dto.requests.UserCreateDto;

public interface RedisService {

    void registerUser(UserCreateDto userCreateDto);

    boolean verifyUser(String email, String activationCode);
}