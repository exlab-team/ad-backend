package com.exlab.incubator.services;

import com.exlab.incubator.configuration.jwt.JwtUtils;
import com.exlab.incubator.configuration.user_details.UserDetailsImpl;
import com.exlab.incubator.dto.requests.LoginRequest;
import com.exlab.incubator.dto.requests.SignupRequest;
import com.exlab.incubator.dto.responses.JwtResponse;
import com.exlab.incubator.dto.responses.MessageResponse;
import com.exlab.incubator.entities.User;
import com.exlab.incubator.repositories.RoleRepository;
import com.exlab.incubator.repositories.UserRepository;
import com.exlab.incubator.services.interfaces.UserService;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
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
    public ResponseEntity<?> authUser(LoginRequest loginRequest) {

        Authentication authentication = getAuthentication(loginRequest);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        return ResponseEntity.ok(new JwtResponse(jwt, userDetails.getId(),
            userDetails.getUsername(), userDetails.getEmail(), userDetails.getPhoneNumber()));
    }

    @Override
    public ResponseEntity<?> registerUser(SignupRequest signupRequest) {
        if (userRepository.existsByUsername(signupRequest.getUsername()))
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is exist"));

        if (userRepository.existsByEmail(signupRequest.getEmail()))
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is exist"));

        createAndSaveUser(signupRequest);

        return ResponseEntity.ok(new MessageResponse("User CREATED"));
    }


    private Authentication getAuthentication(LoginRequest loginRequest) {
        return authenticationManager
            .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
    }

    private String classActivationCode = "";

    private void createAndSaveUser(SignupRequest signupRequest) {
        User user = new User(signupRequest.getUsername(), passwordEncoder.encode(signupRequest.getPassword()),
                             signupRequest.getEmail(), signupRequest.getPhoneNumber(),
                             List.of(roleRepository.findById(1).get()));


        //checking null
        String activationCode = UUID.randomUUID().toString();
        classActivationCode = activationCode;
        String message = String.format("Please, visit next link: http://localhost:8080/authenticate/activate/%s", activationCode);
        mailSender.send( signupRequest.getEmail(), "Activation code", message);

        try {
            Thread.sleep(120000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        userRepository.save(user);
    }

    public ResponseEntity<?> deleteUserById(int id){
        User user = userRepository
            .findById(id)
            .orElseThrow(() -> new UsernameNotFoundException(String.format("User with %d id not found.", id)));

        userRepository.delete(user);
        return ResponseEntity.ok().body(new MessageResponse("DELETED SUCCESSFULLY"));
    }


    public boolean activateUser(String code) {

        if (classActivationCode.equals(code)) {
            return true;
        } else {
            return false;
        }

    }

}
