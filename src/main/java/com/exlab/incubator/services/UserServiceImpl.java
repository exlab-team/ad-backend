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

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;
    private JwtUtils jwtUtils;

    private RoleRepository roleRepository;
    private MailSender mailSender;

    private String lastCreatedActivationCode = "";
    private User userReceivedFromSignupRequest = null;
    private boolean isEmailVerified = false;


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

    private Authentication getAuthentication(LoginRequest loginRequest) {
        return authenticationManager
            .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
    }

    @Override
    public ResponseEntity<?> registerUser(SignupRequest signupRequest) {
        if (userRepository.existsByUsername(signupRequest.getUsername()))
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is exist"));

        if (userRepository.existsByEmail(signupRequest.getEmail()))
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is exist"));

        createAndSaveUser(signupRequest);
        lastCreatedActivationCode = "";

        return isEmailVerified ? ResponseEntity.ok(new MessageResponse("User CREATED")) : ResponseEntity.ok(new MessageResponse("The email hasn't been confirmed. The user isn't saved."));
    }


    private void createAndSaveUser(SignupRequest signupRequest) {
        isEmailVerified = false;
        userReceivedFromSignupRequest = new User(signupRequest.getUsername(), passwordEncoder.encode(signupRequest.getPassword()),
                             signupRequest.getEmail(), signupRequest.getPhoneNumber(),
                             List.of(roleRepository.findById(1).get()));

        sendingAnEmailMessageForEmailVerification(signupRequest.getEmail());
    }

    private void sendingAnEmailMessageForEmailVerification(String email) {
        if (!isEmailVerified) {
            try {
                //it is necessary to add NULL checks
                String activationCode = UUID.randomUUID().toString();
                lastCreatedActivationCode = activationCode;
                String message = String.format("Please, visit next link: http://localhost:8080/authenticate/activate/%s", activationCode);
                mailSender.send(email, "Activation code", message);
                Thread.sleep(300000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public String activateUserByCode(String code) {

        boolean isActivated = lastCreatedActivationCode.equals(code) ? true : false;

        if (isActivated) {
            isEmailVerified = true;
            userRepository.save(userReceivedFromSignupRequest);
            return "Your account has been successfully activated";
        } else {
            isEmailVerified = false;
            sendingAnEmailMessageForEmailVerification(userReceivedFromSignupRequest.getEmail());

            return "This link is outdated. Check your email for a new one";
        }
    }



    public ResponseEntity<?> deleteUserById(int id){
        User user = userRepository
            .findById(id)
            .orElseThrow(() -> new UsernameNotFoundException(String.format("User with %d id not found.", id)));

        userRepository.delete(user);
        return ResponseEntity.ok().body(new MessageResponse("DELETED SUCCESSFULLY"));
    }

}
