package com.exlab.incubator.service.impl;

import com.exlab.incubator.configuration.jwt.JwtUtils;
import com.exlab.incubator.configuration.user_details.UserDetailsImpl;
import com.exlab.incubator.dto.requests.UserLoginDto;
import com.exlab.incubator.dto.responses.UserAccountReadDto;
import com.exlab.incubator.dto.responses.UserReadDto;
import com.exlab.incubator.entity.RedisUser;
import com.exlab.incubator.entity.Role;
import com.exlab.incubator.entity.RoleName;
import com.exlab.incubator.entity.User;
import com.exlab.incubator.entity.UserAccount;
import com.exlab.incubator.exception.EmailVerifiedException;
import com.exlab.incubator.exception.FieldExistsException;
import com.exlab.incubator.exception.UserAccountNotFoundException;
import com.exlab.incubator.exception.UserNotFoundException;
import com.exlab.incubator.repository.UserAccountRepository;
import com.exlab.incubator.repository.UserRepository;
import com.exlab.incubator.service.RoleService;
import com.exlab.incubator.service.UserService;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@EnableScheduling
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserAccountRepository userAccountRepository;
    private final RoleService roleService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserAccountRepository userAccountRepository,
        RoleService roleService, AuthenticationManager authenticationManager, JwtUtils jwtUtils) {
        this.userRepository = userRepository;
        this.userAccountRepository = userAccountRepository;
        this.roleService = roleService;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public UserAccountReadDto loginUser(UserLoginDto userLoginDto) {

        Authentication authentication = getAuthentication(userLoginDto);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        UserAccount userAccount = userAccountRepository.findByUserId(
                userDetails.getId())
            .orElseThrow(() -> new UserAccountNotFoundException("Account not found"));

        return UserAccountReadDto.builder()
            .token(jwt)
            .userId(userDetails.getId())
            .accountId(userAccount.getId())
            .username(userDetails.getUsername())
            .email(userDetails.getEmail())
            .personalAccount(userAccount.getPersonalAccount())
            .build();
    }

    private Authentication getAuthentication(UserLoginDto userLoginDto) {
        return authenticationManager
            .authenticate(new UsernamePasswordAuthenticationToken(userLoginDto.getUsername(),
                userLoginDto.getPassword()));
    }

    @Override
    public void createUser(RedisUser redisUser) {
        User user = userRepository.save(getUserFromRedisUser(redisUser));

        UserAccount userAccount = UserAccount.builder().user(user).build();
        userAccount.setUser(user);
        userAccountRepository.save(userAccount);
    }

    private User getUserFromRedisUser(RedisUser redisUser) {
        Role role = roleService.createRoleIfNotExist(RoleName.USER);
        User user = User.builder()
            .username(redisUser.getUsername())
            .password(redisUser.getPassword())
            .email(redisUser.getEmail())
            .createdAt(redisUser.getCreatedAt())
            .roles(Set.of(role))
            .build();
        return user;
    }

    @Override
    public boolean deleteUserById(long id) {
        return userRepository.findById(id)
            .map(entity -> {
                userRepository.delete(entity);
                userRepository.flush();
                return true;
            })
            .orElse(false);
    }


    @Override
    public void checkingForExistenceInTheDatabase(String username, String email) {
        if (userRepository.existsByUsername(username)) {
            throw new FieldExistsException("Error: Username already exists");
        } else if (userRepository.existsByEmail(email)) {
            throw new FieldExistsException("Error: Email already exists");
        }
    }

    @Override
    public List<UserReadDto> getAllUsers() {

        List<User> users = userRepository.findAll();

        if (users.isEmpty() || users == null){
            throw new UserNotFoundException("There are no users in the database");
        }
        return users.stream().map(user ->
                UserReadDto.builder()
                    .id(user.getId())
                    .userAccountId(user.getUserAccount().getId())
                    .username(user.getUsername())
                    .email(user.getEmail())
                    .createdAt(user.getCreatedAt())
                    .build()
            ).collect(Collectors.toList());
    }

}
