package com.exlab.incubator.service.impl;

import com.exlab.incubator.configuration.jwt.JwtUtils;
import com.exlab.incubator.configuration.user_details.UserDetailsImpl;
import com.exlab.incubator.dto.requests.UserCreateDto;
import com.exlab.incubator.dto.requests.UserLoginDto;
import com.exlab.incubator.dto.responses.UserAccountReadDto;
import com.exlab.incubator.entity.Role;
import com.exlab.incubator.entity.User;
import com.exlab.incubator.entity.UserAccount;
import com.exlab.incubator.exception.EmailVerifiedException;
import com.exlab.incubator.exception.FieldExistsException;
import com.exlab.incubator.exception.UserAccountNotFoundException;
import com.exlab.incubator.repository.UserAccountRepository;
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
    private final UserAccountRepository userAccountRepository;
    private final JwtUtils jwtUtils;
    private final MailSender mailSender;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder,
        AuthenticationManager authenticationManager, JwtUtils jwtUtils,
        UserAccountRepository userAccountRepository, MailSender mailSender) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.userAccountRepository = userAccountRepository;
        this.mailSender = mailSender;
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
        String link = String.format("http://5.101.51.87:8088/api/v1/auth/%s",
            user.getActivationCode());
        mailSender.send(user.getEmail(), "Account activation",
            buildEmail(user.getUsername(), link));
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

    private void checkingForNecessityForDeletingAUser(UserCreateDto userCreateDto,
        Boolean existsByUsername, Boolean existsByEmail) {
        if (existsByUsername && existsByEmail) {
            User user = userRepository.findByUsername(userCreateDto.getUsername()).get();
            if (!user.isEmailVerified()) {
                checkingTimeToDeleteAUser(user);
            }
        }
    }

    private String buildEmail(String name, String link) {
        return
            "<div style=\"font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#0b0c0c\">\n"
                +
                "\n" +
                "<span style=\"display:none;font-size:1px;color:#fff;max-height:0\"></span>\n" +
                "\n" +
                "  <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n"
                +
                "    <tbody><tr>\n" +
                "      <td width=\"100%\" height=\"53\" bgcolor=\"#0b0c0c\">\n" +
                "        \n" +
                "        <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;max-width:580px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n"
                +
                "          <tbody><tr>\n" +
                "            <td width=\"70\" bgcolor=\"#0b0c0c\" valign=\"middle\">\n" +
                "                <table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n"
                +
                "                  <tbody><tr>\n" +
                "                    <td style=\"padding-left:10px\">\n" +
                "                  \n" +
                "                    </td>\n" +
                "                    <td style=\"font-size:28px;line-height:1.315789474;Margin-top:4px;padding-left:10px\">\n"
                +
                "                      <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff;text-decoration:none;vertical-align:top;display:inline-block\">Confirm your email</span>\n"
                +
                "                    </td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "              </a>\n" +
                "            </td>\n" +
                "          </tr>\n" +
                "        </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n"
                +
                "    <tbody><tr>\n" +
                "      <td width=\"10\" height=\"10\" valign=\"middle\"></td>\n" +
                "      <td>\n" +
                "        \n" +
                "                <table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n"
                +
                "                  <tbody><tr>\n" +
                "                    <td bgcolor=\"#1D70B8\" width=\"100%\" height=\"10\"></td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\" height=\"10\"></td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "\n" +
                "\n" +
                "\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n"
                +
                "    <tbody><tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "      <td style=\"font-family:Helvetica,Arial,sans-serif;font-size:19px;line-height:1.315789474;max-width:560px\">\n"
                +
                "        \n" +
                "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Hi "
                + name
                + ",</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> Thank you for registering. Please click on the below link to activate your account: </p><blockquote style=\"Margin:0 0 20px 0;border-left:10px solid #b1b4b6;padding:15px 0 0.1px 15px;font-size:19px;line-height:25px\"><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> <a href=\""
                + link
                + "\">Activate Now</a> </p></blockquote>\n Link will expire in 5 minutes. <p>See you soon</p>"
                +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "  </tbody></table><div class=\"yj6qo\"></div><div class=\"adL\">\n" +
                "\n" +
                "</div></div>";
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
