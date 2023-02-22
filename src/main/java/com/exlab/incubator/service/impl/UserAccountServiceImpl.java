package com.exlab.incubator.service.impl;

import com.exlab.incubator.configuration.jwt.JwtUtils;
import com.exlab.incubator.entity.PersonalAccount;
import com.exlab.incubator.entity.Tariff;
import com.exlab.incubator.exception.UserNotFoundException;
import com.exlab.incubator.repository.TariffRepository;
import com.exlab.incubator.repository.UserAccountRepository;
import com.exlab.incubator.service.TariffService;
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

    @Autowired
    public UserAccountServiceImpl(UserAccountRepository userAccountRepository) {
        this.userAccountRepository = userAccountRepository;
    }

    @Override
    @Transactional
    public boolean selectTariff(Long account_id, Tariff tariff) {
        Integer result = userAccountRepository.findById(account_id)
            .map(account -> {
                account.setTariff(tariff);
                account.setPersonalAccount(
                    PersonalAccount.builder()
                        .accountNumber(UUID.randomUUID().toString())
                        .createdAt(Instant.now())
                        .build());
                account.setModifiedAt(Instant.now());
                return userAccountRepository.updateWithTariff(account);
            }).orElseThrow(() -> new UserNotFoundException("User account doesn't exist"));
        return result > 0;
    }
}
