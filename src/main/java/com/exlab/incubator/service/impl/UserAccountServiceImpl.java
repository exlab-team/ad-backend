package com.exlab.incubator.service.impl;

import com.exlab.incubator.entity.Tariff;
import com.exlab.incubator.entity.User;
import com.exlab.incubator.entity.UserAccount;
import com.exlab.incubator.exception.ActivationCodeException;
import com.exlab.incubator.exception.ActivationCodeNotFoundException;
import com.exlab.incubator.exception.UserAccountNotFoundException;
import com.exlab.incubator.repository.UserAccountRepository;
import com.exlab.incubator.repository.UserRepository;
import com.exlab.incubator.service.UserAccountService;
import java.time.Instant;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class UserAccountServiceImpl implements UserAccountService {

    private final UserAccountRepository userAccountRepository;
    private final UserRepository userRepository;

    @Autowired
    public UserAccountServiceImpl(UserAccountRepository userAccountRepository,
        UserRepository userRepository) {
        this.userAccountRepository = userAccountRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public boolean establishTariffToUser(Long account_id, Tariff tariff) {
        Integer result = userAccountRepository.findById(account_id)
            .map(account -> {
                account.setTariff(tariff);
                account.setPersonalAccount(UUID.randomUUID().toString());
                account.setModifiedAt(Instant.now());
                return userAccountRepository.updateWithTariff(account);
            }).orElseThrow(() -> new UserAccountNotFoundException("User account doesn't exist"));
        return result > 0;
    }

    @Override
    @Transactional
    public long activateUserAccountByCode(String activationCode) {
        User user = userRepository.findByActivationCode(activationCode)
            .orElseThrow(() -> new ActivationCodeNotFoundException("Activation code is invalid"));

        if (user.isEmailVerified()) {
            throw new ActivationCodeException("Account has already activated");
        } else {
            UserAccount userAccount = UserAccount.builder()
                .user(user)
                .build();
            user.setEmailVerified(true);
            userAccount.setUser(user);
            return userAccountRepository.save(userAccount).getId();
        }
    }
}
