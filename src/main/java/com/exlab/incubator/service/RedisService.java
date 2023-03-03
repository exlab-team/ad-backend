package com.exlab.incubator.service;

import com.exlab.incubator.entity.RedisUser;

public interface RedisService {

    String saveUser(RedisUser redisUser);

    RedisUser getRedisUserByEmail(String email);

    void deleteUser(String email);

    boolean redisUserExists(String email);
}