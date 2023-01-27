package com.exlab.incubator.service.impl;

import com.exlab.incubator.configuration.jwt.JwtUtils;
import com.exlab.incubator.configuration.user_details.UserDetailsImpl;
import com.exlab.incubator.dto.requests.UserLoginDto;
import com.exlab.incubator.dto.requests.UserCreateDto;
import com.exlab.incubator.dto.responses.UserDto;
import com.exlab.incubator.dto.responses.MessageDto;
import com.exlab.incubator.entity.User;
import com.exlab.incubator.entity.UserAccount;
import com.exlab.incubator.exception.UserNotFoundException;
import com.exlab.incubator.repository.RoleRepository;
import com.exlab.incubator.repository.UserAccountRepository;
import com.exlab.incubator.repository.UserRepository;
import com.exlab.incubator.service.MailSender;
import com.exlab.incubator.service.UserService;
import com.exlab.incubator.exception.FieldExistsException;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
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
    private final UserAccountRepository userAccountRepository;
    private final MailSender mailSender;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder,
        AuthenticationManager authenticationManager, JwtUtils jwtUtils,
        RoleRepository roleRepository,
        UserAccountRepository userAccountRepository, MailSender mailSender) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.roleRepository = roleRepository;
        this.userAccountRepository = userAccountRepository;
        this.mailSender = mailSender;
    }

    @Override
    public UserDto loginUser(UserLoginDto userLoginDto) {

        Authentication authentication = getAuthentication(userLoginDto);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        return new UserDto(jwt, userDetails.getId(), userDetails.getUsername(),
            userDetails.getEmail(),
            userDetails.isEmailVerified());  // добавила
    }

    private Authentication getAuthentication(UserLoginDto userLoginDto) {
        return authenticationManager
            .authenticate(new UsernamePasswordAuthenticationToken(userLoginDto.getUsername(),
                userLoginDto.getPassword()));
    }

    @Override
    public MessageDto createUser(UserCreateDto userCreateDto) {
        if (userRepository.existsByUsername(userCreateDto.getUsername())) {
            throw new FieldExistsException(
                "Error: Username already exists"); //вынести в контроллер и возвращать BadRequest
        }

        if (userRepository.existsByEmail(userCreateDto.getEmail())) {
            throw new FieldExistsException("Error: Email already exists");
        }

        createAndSaveUser(userCreateDto);

        return new MessageDto("Mail confirmation is expected"); // или UserReadDto или id
    }

    private void createAndSaveUser(UserCreateDto userCreateDto) {
        User user = User.builder()
            .username(userCreateDto.getUsername())
            .password(passwordEncoder.encode(userCreateDto.getPassword()))
            .email(userCreateDto.getEmail())
            .emailVerified(false)
            .createdAt(Instant.now())
            .roles(List.of(roleRepository.findById(1).get()))
            .build();

        sendingAnEmailMessageForEmailVerification(getUserWithTheNewActivationCode(user));
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
    public String activateUserByCode(String activationCode) {

        Optional<User> optionalUser = userRepository.findByActivationCode(activationCode);
        if (optionalUser.isEmpty()) {
            return "This link is outdated.";
        }
        User user = optionalUser.get();

        if (user.isEmailVerified()) {
            return "Your account is active.";
        } else {
            user.setEmailVerified(true);
            userRepository.save(user);
            userAccountRepository.save(UserAccount.builder()
                .userId(user.getId())
                .build());
            return "Your account has been successfully activated.";
        }
    }


    public MessageDto deleteUserById(long id) {
        User user = userRepository.findById(id)
            .orElseThrow(
                () -> new UserNotFoundException(String.format("User with %d id not found.", id)));

        Optional<UserAccount> userAccount = userAccountRepository.findByUserId(id);
        userAccount.ifPresent(account -> userAccountRepository.delete(account));
        userRepository.delete(user);
        return new MessageDto(String.format("User with id - %d - deleted successfully", id));
    }

    @Scheduled(fixedDelay = 30000)
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
