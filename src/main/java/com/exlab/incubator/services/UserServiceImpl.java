package com.exlab.incubator.services;

import com.exlab.incubator.configuration.jwt.JwtUtils;
import com.exlab.incubator.configuration.user_details.UserDetailsImpl;
import com.exlab.incubator.dto.requests.LoginRequest;
import com.exlab.incubator.dto.requests.SignupRequest;
import com.exlab.incubator.dto.responses.JwtResponse;
import com.exlab.incubator.dto.responses.MessageResponse;
import com.exlab.incubator.entities.PersonalAccount;
import com.exlab.incubator.entities.Role;
import com.exlab.incubator.entities.User;
import com.exlab.incubator.repositories.RoleRepository;
import com.exlab.incubator.repositories.UserRepository;
import com.exlab.incubator.services.interfaces.UserService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;
    private JwtUtils jwtUtils;

    private RoleRepository roleRepository;


    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder,
        AuthenticationManager authenticationManager, JwtUtils jwtUtils, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.roleRepository = roleRepository;
    }

    @Override
    public ResponseEntity<?> authUser(LoginRequest loginRequest) {
        Authentication authentication = getAuthentication(loginRequest);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        return ResponseEntity.ok(new JwtResponse(jwt, userDetails.getId(), userDetails.getUsername(), userDetails.getEmail(),
            userDetails.getPhoneNumber()));
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

    @Override
    public User getUserByUsername(String username) {
        Optional<User> user = userRepository.findByUsername(username);

        if (!user.isPresent())
            throw new UsernameNotFoundException("User with username - " + username + " - not found.");

        return user.get();
    }


    private Authentication getAuthentication(LoginRequest loginRequest) {
        return authenticationManager
            .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
    }

    private void createAndSaveUser(SignupRequest signupRequest) {
        Role role = roleRepository.findById(1).get();
        List<Role> roles = new ArrayList<>();
        roles.add(role);


        User user = new User(signupRequest.getUsername(),
                            passwordEncoder.encode(signupRequest.getPassword()),
                            signupRequest.getEmail(),
                            signupRequest.getPhoneNumber(),
                            roles);
        userRepository.save(user);
    }

}
