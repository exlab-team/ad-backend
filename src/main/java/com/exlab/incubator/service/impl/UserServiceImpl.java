package com.exlab.incubator.service.impl;

import com.exlab.incubator.configuration.jwt.JwtUtils;
import com.exlab.incubator.configuration.user_details.UserDetailsImpl;
import com.exlab.incubator.dto.requests.UserLoginDto;
import com.exlab.incubator.dto.requests.UserCreateDto;
import com.exlab.incubator.dto.responses.UserDto;
import com.exlab.incubator.dto.responses.MessageDto;
import com.exlab.incubator.entity.User;
import com.exlab.incubator.exception.UserNotFoundException;
import com.exlab.incubator.repository.RoleRepository;
import com.exlab.incubator.repository.UserRepository;
import com.exlab.incubator.service.MailSender;
import com.exlab.incubator.service.UserService;
import com.exlab.incubator.exception.FieldExistsException;
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

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;
    private JwtUtils jwtUtils;

    private RoleRepository roleRepository;
    private MailSender mailSender;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder,
        AuthenticationManager authenticationManager, JwtUtils jwtUtils, RoleRepository roleRepository,
        MailSender mailSender) {
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

        return new UserDto(jwt, userDetails.getId(), userDetails.getUsername(), userDetails.getEmail());
    }

    private Authentication getAuthentication(UserLoginDto userLoginDto) {
        return authenticationManager
            .authenticate(new UsernamePasswordAuthenticationToken(userLoginDto.getUsername(), userLoginDto.getPassword()));
    }

    @Override
    public MessageDto createUser(UserCreateDto userCreateDto) {
        if (userRepository.existsByUsername(userCreateDto.getUsername()))
            throw new FieldExistsException("Error: Username already exists");

        if (userRepository.existsByEmail(userCreateDto.getEmail()))
            throw new FieldExistsException("Error: Email already exists");

        createAndSaveUser(userCreateDto);

        return new MessageDto("Mail confirmation is expected");
    }

    private void createAndSaveUser(UserCreateDto userCreateDto) {
        User user = User.builder()
            .username(userCreateDto.getUsername())
            .password(passwordEncoder.encode(userCreateDto.getPassword()))
            .email(userCreateDto.getEmail())
            .isConfirmed(false)
            .createdAt(new Date())
            .roles(List.of(roleRepository.findById(1).get()))
            .build();

        sendingAnEmailMessageForEmailVerification(getUserWithTheNewActivationCode(user));
    }

    private User getUserWithTheNewActivationCode(User user){
        user.setActivationCode(UUID.randomUUID().toString());
        user.setTimeOfSendingTheConfirmationLink(new Date());
        return userRepository.save(user);
    }

    private void sendingAnEmailMessageForEmailVerification(User user) {
            String message = String.format("Пожалуйста, перейдите по данной ссылке для "
                + "активации вашего аккаунта: http://localhost:8080/users/%s", user.getActivationCode());
            mailSender.send(user.getEmail(), "Activation code", message);
    }

    @Override
    public String activateUserByCode(String activationCode) {

        Optional<User> optionalUser = userRepository.findByActivationCode(activationCode);
        if (!optionalUser.isPresent()){
            return "This link is outdated.";
        }
        User user = optionalUser.get();

        if (user.getIsConfirmed()) {
            return "Your account is active.";
        } else {
            user.setIsConfirmed(true);
            userRepository.save(user);
            return "Your account has been successfully activated.";
        }
    }


    public String deleteUserById(int id){
        User user = userRepository.findById(id)
            .orElseThrow(() -> new UserNotFoundException(String.format("User with %d id not found.", id)));

        userRepository.delete(user);
        return String.format("User with id - %d - deleted successfully", id);
    }

    @Scheduled(fixedDelay = 30000)
    private void deletingUsersWithAnOutdatedLink(){
        long currentTime = new Date().getTime();
        List<User> users = userRepository.findAll().stream().filter((user) -> user.getIsConfirmed() == false)
            .collect(Collectors.toList());

        users.stream().forEach(user -> {
            if ((currentTime  - user.getTimeOfSendingTheConfirmationLink().getTime()) >= 300000) {
                userRepository.delete(user);
            }
        });
    }
}
