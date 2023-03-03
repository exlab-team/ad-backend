package com.exlab.incubator.service.impl;

import com.exlab.incubator.entity.RedisUser;
import com.exlab.incubator.service.RedisService;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RedisServiceImpl implements RedisService {

    @Autowired
    private RedisTemplate<String, Object> template;

    @Override
    public String saveUser(RedisUser redisUser){
        String key = redisUser.getEmail();
        template.opsForValue().set(key, redisUser, 5, TimeUnit.MINUTES);
        log.info("User with username - " + redisUser.getUsername()
            + ", and email - " + redisUser.getEmail() + " has been saved to the REDIS database.");
        return redisUser.getUsername();
    }


    @Override
    public RedisUser getRedisUserByEmail(String email) {
        return (RedisUser) template.opsForValue().get(email);
    }

    @Override
    public void deleteUser(String email) {
        template.opsForValue().getAndDelete(email);
        log.info("The user's activation was successful. He has been removed from the REDIS database.");
    }

    @Override
    public boolean redisUserExists(String activationCode) {
        RedisUser redisUser = (RedisUser) template.opsForValue().get(activationCode);
        return redisUser != null;
    }
}
