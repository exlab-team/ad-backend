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
import java.util.Date;
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

        return ResponseEntity.ok(new MessageResponse("Mail confirmation is expected"));
    }


    private void createAndSaveUser(SignupRequest signupRequest) {

        User user = new User(signupRequest.getUsername(), passwordEncoder.encode(signupRequest.getPassword()),
            signupRequest.getEmail(), false, new Date(),
            List.of(roleRepository.findById(1).get()));

        sendingAnEmailMessageForEmailVerification(user, signupRequest.getEmail());
    }

    private void sendingAnEmailMessageForEmailVerification(User user, String email) {

        try {
            //it is necessary to add NULL checks
            String activationCode = UUID.randomUUID().toString();
            user.setActivationCode(activationCode);
            userRepository.save(user);

            String message = String.format("Please, visit next link: http://localhost:8080/authenticate/activate/%s.%s", user.getUsername(), activationCode);
            mailSender.send(email, "Activation code", message);
            Thread.sleep(300000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


    @Override
    public String activateUserByCode(String username, String code) {

        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found."));

        boolean areTheCodesEqual = user.getActivationCode().equals(code);

        if (areTheCodesEqual && !user.getIsConfirmed()) {
            user.setIsConfirmed(true);
            userRepository.save(user);
            return "Your account has been successfully activated";
        }else if (areTheCodesEqual && user.getIsConfirmed()){
            return "Your account has been successfully activated";
        } else {
            sendingAnEmailMessageForEmailVerification(user, user.getEmail());
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
