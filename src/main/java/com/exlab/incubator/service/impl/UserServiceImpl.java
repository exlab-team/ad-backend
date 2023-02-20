package com.exlab.incubator.service.impl;

import com.exlab.incubator.configuration.jwt.JwtUtils;
import com.exlab.incubator.configuration.user_details.UserDetailsImpl;
import com.exlab.incubator.dto.requests.UserCreateDto;
import com.exlab.incubator.dto.requests.UserLoginDto;
import com.exlab.incubator.dto.responses.UserDto;
import com.exlab.incubator.entity.Role;
import com.exlab.incubator.entity.User;
import com.exlab.incubator.entity.UserAccount;
import com.exlab.incubator.exception.ActivationCodeException;
import com.exlab.incubator.exception.EmailVerifiedException;
import com.exlab.incubator.exception.FieldExistsException;
import com.exlab.incubator.exception.UserNotFoundException;
import com.exlab.incubator.repository.RoleRepository;
import com.exlab.incubator.repository.UserRepository;
import com.exlab.incubator.service.MailSender;
import com.exlab.incubator.service.UserService;
import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@EnableScheduling
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final RoleRepository roleRepository;
    private final MailSender mailSender;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder,
        AuthenticationManager authenticationManager, JwtUtils jwtUtils,
        RoleRepository roleRepository, MailSender mailSender) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.roleRepository = roleRepository;
        this.mailSender = mailSender;
    }

    @Override
    public UserDto loginUser(UserLoginDto userLoginDto) {

        Authentication authentication = getAuthentication(userLoginDto);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        if (!userDetails.isEmailVerified()) {
            throw new EmailVerifiedException("Email doesn't verified");
        }

        return new UserDto(jwt, userDetails.getId(), userDetails.getUsername(),
            userDetails.getEmail());
    }

    private Authentication getAuthentication(UserLoginDto userLoginDto) {
        return authenticationManager
            .authenticate(new UsernamePasswordAuthenticationToken(userLoginDto.getUsername(),
                userLoginDto.getPassword()));
    }

    @Override
    public Long createUser(UserCreateDto userCreateDto, Role role) {

        checkingForNecessityForDeletingAUser(userCreateDto,
            userRepository.existsByUsername(userCreateDto.getUsername()),
            userRepository.existsByEmail(userCreateDto.getEmail()));

        if (userRepository.existsByUsername(userCreateDto.getUsername())) {
            throw new FieldExistsException("Error: Username already exists");
        } else if (userRepository.existsByEmail(userCreateDto.getEmail())) {
            throw new FieldExistsException("Error: Email already exists");
        }

        return createAndSaveUser(userCreateDto, role);
    }


    private Long createAndSaveUser(UserCreateDto userCreateDto, Role role) {
        User user = User.builder()
            .username(userCreateDto.getUsername())
            .password(passwordEncoder.encode(userCreateDto.getPassword()))
            .email(userCreateDto.getEmail())
            .emailVerified(false)
            .createdAt(Instant.now())
            .roles(Set.of(role))
            .build();

        User savedUser = getUserWithTheNewActivationCode(user);
        sendingAnEmailMessageForEmailVerification(savedUser);
        return savedUser.getId();
    }

    private User getUserWithTheNewActivationCode(User user) {
        user.setActivationCode(UUID.randomUUID().toString());
        user.setTimeOfSendingVerificationLink(Instant.now());
        return userRepository.save(user);
    }

    private void sendingAnEmailMessageForEmailVerification(User user) {
        String message = String.format("Пожалуйста, перейдите по данной ссылке для "
                + "активации вашего аккаунта: http://localhost:8088/api/v1/auth/%s",
            user.getActivationCode());
        mailSender.send(user.getEmail(), "Activation code", message);
    }

    @Override
    public boolean activateUserByCode(String activationCode) {

        User user = userRepository.findByActivationCode(activationCode)
            .orElseThrow(() -> new ActivationCodeException("Activation code is invalid"));

        if (user.isEmailVerified()) {
            return false;
        } else {
            user.setEmailVerified(true);
            UserAccount userAccount = UserAccount.builder()
                .user(user)
                .build();
            user.setUserAccount(userAccount);
            userRepository.save(user);
            return true;
        }
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

    private void checkingForNecessityForDeletingAUser(UserCreateDto userCreateDto, Boolean existsByUsername, Boolean existsByEmail) {
        if (existsByUsername && existsByEmail) {
            User user = userRepository.findByUsername(userCreateDto.getUsername()).get();
            if (!user.isEmailVerified()) {
                checkingTimeToDeleteAUser(user);
            }
        }
    }

    private void checkingTimeToDeleteAUser(User user) {
        long currentTime = Instant.now().toEpochMilli();
        if ((currentTime - user.getTimeOfSendingVerificationLink().toEpochMilli())
            >= 300000) {
            userRepository.delete(user);
        }
    }

    @Scheduled(fixedDelay = (60000 * 60 * 24))
    private void deletingUsersWithAnOutdatedLink() {
        long currentTime = Instant.now().toEpochMilli();
        List<User> users = userRepository.findAll().stream()
            .filter((user) -> !user.isEmailVerified())
            .toList();

        users.forEach(user -> {
            if ((currentTime - user.getTimeOfSendingVerificationLink().toEpochMilli()) >= 300000) {
                userRepository.delete(user);
            }
        });
    }
}
