package com.exlab.incubator.service.impl;

import com.exlab.incubator.configuration.jwt.JwtUtils;
import com.exlab.incubator.entity.Tariff;
import com.exlab.incubator.entity.User;
import com.exlab.incubator.entity.UserAccount;
import com.exlab.incubator.repository.UserAccountRepository;
import com.exlab.incubator.repository.UserRepository;
import com.exlab.incubator.service.UserAccountService;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserAccountServiceImpl implements UserAccountService {

    private final UserAccountRepository userAccountRepository;
    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;

    @Autowired
    public UserAccountServiceImpl(UserAccountRepository userAccountRepository, UserRepository userRepository, JwtUtils jwtUtils) {
        this.userAccountRepository = userAccountRepository;
        this.userRepository = userRepository;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public boolean setTariff(Tariff tariff, String jwt) {
        System.out.println("Username: " + jwtUtils.getUserNameFromJwtToken(jwt));
        System.out.println("Tariff: " + tariff);

        //нужны проверки на NULL
        User user = userRepository.findByUsername(jwtUtils.getUserNameFromJwtToken(jwt)).get();
        Optional<UserAccount> optionalUserAccount = userAccountRepository.findByUserId(user.getId());

        if (!optionalUserAccount.isEmpty()){
            UserAccount userAccount = optionalUserAccount.get();
            System.out.println(userAccount.getId());
            userAccount.setTariff(tariff);
            userAccountRepository.save(userAccount);
            return true;
        }

        return false;
    }
}
