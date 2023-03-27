package com.exlab.incubator.service;

import com.exlab.incubator.dto.requests.UserLoginDto;
import com.exlab.incubator.dto.responses.UserAccountReadDto;
import com.exlab.incubator.dto.responses.UserReadDto;
import com.exlab.incubator.entity.RedisUser;
import com.exlab.incubator.entity.User;
import java.util.List;

public interface UserService {

    UserAccountReadDto loginUser(UserLoginDto userLoginDto);

    boolean deleteUserById(long id);

    void createUser(RedisUser redisUser);

    void checkingForExistenceInTheDatabase(String username, String email);

    List<UserReadDto> getAllUsers();
}
