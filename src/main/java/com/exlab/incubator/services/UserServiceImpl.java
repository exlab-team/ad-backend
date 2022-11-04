package com.exlab.incubator.services;

import com.exlab.incubator.entities.User;
import com.exlab.incubator.repositories.UserRepository;
import com.exlab.incubator.services.service_interfaces.UserService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
