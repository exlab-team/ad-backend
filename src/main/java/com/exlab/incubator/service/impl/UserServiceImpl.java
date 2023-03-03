package com.exlab.incubator.service.impl;

import com.exlab.incubator.configuration.jwt.JwtUtils;
import com.exlab.incubator.configuration.user_details.UserDetailsImpl;
import com.exlab.incubator.dto.requests.UserLoginDto;
import com.exlab.incubator.dto.responses.UserAccountReadDto;
import com.exlab.incubator.entity.UserAccount;
import com.exlab.incubator.exception.EmailVerifiedException;
import com.exlab.incubator.exception.UserAccountNotFoundException;
import com.exlab.incubator.repository.UserAccountRepository;
import com.exlab.incubator.repository.UserRepository;
import com.exlab.incubator.service.UserService;
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
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserAccountRepository userAccountRepository,
        AuthenticationManager authenticationManager, JwtUtils jwtUtils) {
        this.userRepository = userRepository;
        this.userAccountRepository = userAccountRepository;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public UserAccountReadDto loginUser(UserLoginDto userLoginDto) {

        Authentication authentication = getAuthentication(userLoginDto);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        if (!userDetails.isEmailVerified()) {
            throw new EmailVerifiedException("Email doesn't verified");
        }

        UserAccount userAccount = userAccountRepository.findByUserId(
                userDetails.getId())
            .orElseThrow(() -> new UserAccountNotFoundException("Account not found"));

        return UserAccountReadDto.builder()
            .token(jwt)
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
    public boolean deleteUserById(long id) {
        return userRepository.findById(id)
            .map(entity -> {
                userRepository.delete(entity);
                userRepository.flush();
                return true;
            })
            .orElse(false);
    }

}
