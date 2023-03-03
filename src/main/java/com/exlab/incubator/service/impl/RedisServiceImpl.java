package com.exlab.incubator.service.impl;

import com.exlab.incubator.dto.requests.UserCreateDto;
import com.exlab.incubator.entity.RedisUser;
import com.exlab.incubator.exception.ActivationCodeException;
import com.exlab.incubator.exception.FieldExistsException;
import com.exlab.incubator.service.MailSender;
import com.exlab.incubator.service.RedisService;
import com.exlab.incubator.service.UserService;
import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class RedisServiceImpl implements RedisService {

    @Autowired
    private RedisTemplate<String, Object> template;

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final MailSender mailSender;


    @Autowired
    public RedisServiceImpl(UserService userService,
        PasswordEncoder passwordEncoder, MailSender mailSender) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.mailSender = mailSender;
    }


    @Override
    public void registerUser(UserCreateDto userCreateDto) {

        userService.checkingForExistenceInTheDatabase(userCreateDto.getUsername(), userCreateDto.getEmail());

        RedisUser redisUser = getRedisUserFromUserCreateDTO(userCreateDto);

        if (redisUserExists(redisUser.getEmail())){
            log.info("The user with - " + redisUser.getEmail() + " - email is already in the database.");
            throw new FieldExistsException("User with this email is already registered");
        }

        saveRedisUser(redisUser);
        sendingAnEmailMessageForEmailVerification(redisUser);
    }

    private RedisUser getRedisUserFromUserCreateDTO(UserCreateDto userCreateDto) {
        RedisUser redisUser = RedisUser.builder()
            .username(userCreateDto.getUsername())
            .password(passwordEncoder.encode(userCreateDto.getPassword()))
            .email(userCreateDto.getEmail())
            .emailVerified(false)
            .createdAt(Instant.now())
            .timeOfSendingVerificationLink(Instant.now())
            .activationCode(UUID.randomUUID().toString())
            .build();
        return redisUser;
    }

    private boolean redisUserExists(String email) {
        RedisUser redisUser = (RedisUser) template.opsForValue().get(email);
        return redisUser != null;
    }

    private void saveRedisUser(RedisUser redisUser){
        String key = redisUser.getEmail();
        template.opsForValue().set(key, redisUser, 5, TimeUnit.MINUTES);
        log.info("User with username - " + redisUser.getUsername()
            + ", and email - " + redisUser.getEmail() + " has been saved to the REDIS database.");
    }

    private void sendingAnEmailMessageForEmailVerification(RedisUser user) {
        String link = String.format("http://localhost:8088/api/v1/auth/%s?email=%s",
            user.getActivationCode(), user.getEmail());

        mailSender.send(user.getEmail(), "Account activation", buildEmail(user.getUsername(), link));
    }

    @Transactional
    @Override
    public void verifyUser(String email, String activationCode) {
        RedisUser redisUser = getRedisUserByEmail(email);

        if (redisUser != null && redisUser.getActivationCode().equals(activationCode)){
            deleteRedisUser(email);
            userService.createUser(redisUser);
        } else {
            throw new ActivationCodeException("Activation code is invalid");
        }

    }


    private RedisUser getRedisUserByEmail(String email) {
        return (RedisUser) template.opsForValue().get(email);
    }


    private void deleteRedisUser(String email) {
        template.opsForValue().getAndDelete(email);
        log.info("The user's activation was successful. He has been removed from the REDIS database.");
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
}
