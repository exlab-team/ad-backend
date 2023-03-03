package com.exlab.incubator.service;

import com.exlab.incubator.dto.requests.UserLoginDto;
import com.exlab.incubator.dto.responses.UserAccountReadDto;

public interface UserService {

    UserAccountReadDto loginUser(UserLoginDto userLoginDto);

    boolean deleteUserById(long id);

}
