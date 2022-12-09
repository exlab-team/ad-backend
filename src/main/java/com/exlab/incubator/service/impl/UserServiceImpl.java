package com.exlab.incubator.service.impl;

import com.exlab.incubator.configuration.jwt.JwtUtils;
import com.exlab.incubator.configuration.user_details.UserDetailsImpl;
import com.exlab.incubator.dto.requests.UserLoginDto;
import com.exlab.incubator.dto.requests.UserCreateDto;
import com.exlab.incubator.dto.responses.UserDto;
import com.exlab.incubator.dto.responses.MessageDto;
import com.exlab.incubator.entity.User;
import com.exlab.incubator.repository.RoleRepository;
import com.exlab.incubator.repository.UserRepository;
import com.exlab.incubator.service.MailSender;
import com.exlab.incubator.service.UserService;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
    public ResponseEntity<MessageDto> createUser(UserCreateDto userCreateDto) {
        if (userRepository.existsByUsername(userCreateDto.getUsername()))
            return ResponseEntity.badRequest().body(new MessageDto("Error: Username is exist"));

        if (userRepository.existsByEmail(userCreateDto.getEmail()))
            return ResponseEntity.badRequest().body(new MessageDto("Error: Email is exist"));

        createAndSaveUser(userCreateDto);

        return ResponseEntity.ok(new MessageDto("Mail confirmation is expected"));
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

        sendingAnEmailMessageForEmailVerification(getUserWithTheNewActivationCode(user), userCreateDto.getEmail());
    }

    private User getUserWithTheNewActivationCode(User user){
        String activationCode = UUID.randomUUID().toString();
        user.setActivationCode(activationCode);
        user.setTimeOfSendingTheConfirmationLink(new Date());
        return userRepository.save(user);
    }

    private void sendingAnEmailMessageForEmailVerification(User user, String email) {
            String encryptUsername = encryptTheUsername(user.getUsername());
            String message = String.format("Please, visit next link: http://localhost:8080/users/%s.%s", encryptUsername, user.getActivationCode());
            mailSender.send(email, "Activation code", message);
    }

    @Override
    public String activateUserByCode(String usernamePlusCode) {
        int charAt = usernamePlusCode.indexOf(".");
        String decryptUsername = decryptTheUsername(usernamePlusCode.substring(0, charAt));
        String code = usernamePlusCode.substring(charAt + 1);

        User user = userRepository.findByUsername(decryptUsername)
            .orElseThrow(() -> new UsernameNotFoundException(String.format("User %s not found.", decryptUsername)));

        if (user.getIsConfirmed()) return "Your account is active";

        return activatingAnAccountOrSendingANewLink(user, user.getActivationCode().equals(code));
    }

    private String activatingAnAccountOrSendingANewLink(User user, boolean areTheCodesEqual) {
        if (areTheCodesEqual && !user.getIsConfirmed()) {
            user.setIsConfirmed(true);
            userRepository.save(user);
            return "Your account has been successfully activated";
        } else {
            sendingAnEmailMessageForEmailVerification(getUserWithTheNewActivationCode(user), user.getEmail());
            return "This link is outdated. Check your email for a new one";
        }
    }

    @Override
    public ResponseEntity<MessageDto> resendingTheVerificationLink(String email) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        sendingAnEmailMessageForEmailVerification(getUserWithTheNewActivationCode(user), email);
        return ResponseEntity.ok(new MessageDto("The resending of the link to the email was successful"));
    }

    private String encryptTheUsername(String username){
        StringBuilder stringBuilder = new StringBuilder();
        username.chars().mapToObj(c -> (char) ++c).forEach(c -> stringBuilder.append(c));
        return stringBuilder.toString();
    }

    private String decryptTheUsername(String username){
        StringBuilder stringBuilder = new StringBuilder();
        username.chars().mapToObj(c -> (char) --c).forEach(c -> stringBuilder.append(c));
        return stringBuilder.toString();
    }

    public String deleteUserById(int id){
        User user = userRepository.findById(id)
            .orElseThrow(() -> new UsernameNotFoundException(String.format("User with %d id not found.", id)));

        userRepository.delete(user);
        return String.format("User with id - %d - deleted successfully", id);
    }

    @Scheduled(fixedDelay = 30000)
    private void checkingUsersForTheEndOfTheVerificationTime(){
        long currentTime = new Date().getTime();
        List<User> users = userRepository.findAll().stream().filter((user) -> user.getIsConfirmed() == false)
            .collect(Collectors.toList());

        users.stream().forEach(user -> {
            if ((currentTime  - user.getCreatedAt().getTime()) >= (3600000 * 24)) {
                userRepository.delete(user);
            }
        });
    }

    @Scheduled(fixedDelay = 30000)
    private void checkingForLinkOutdated(){
        long currentTime = new Date().getTime();
        List<User> users = userRepository.findAll().stream().filter((user) -> user.getIsConfirmed() == false)
            .collect(Collectors.toList());

        users.stream().forEach(user -> {
            if ((currentTime  - user.getTimeOfSendingTheConfirmationLink().getTime()) >= 300000) {
                user.setActivationCode("outdated");
                userRepository.save(user);
            }
        });
    }
}
