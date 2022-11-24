package com.exlab.incubator.service.impl;

import com.exlab.incubator.configuration.jwt.JwtUtils;
import com.exlab.incubator.dto.requests.TariffRequest;
import com.exlab.incubator.entity.Tariff;
import com.exlab.incubator.entity.User;
import com.exlab.incubator.repository.UserRepository;
import com.exlab.incubator.service.TariffPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class TariffPlanServiceImpl implements TariffPlanService {

    private UserRepository userRepository;
    private JwtUtils jwtUtils;

    @Autowired
    public TariffPlanServiceImpl(UserRepository userRepository, JwtUtils jwtUtils) {
        this.userRepository = userRepository;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public ResponseEntity<?> setTariffPlanToTheUser(TariffRequest tariffRequest, String jwt) {
        String tariff = tariffRequest.getTariff();
        String username = jwtUtils.getUserNameFromJwtToken(jwt);
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        for (Tariff t: Tariff.values()){
            if (t.toString().equalsIgnoreCase(tariff)){
                user.setTariff(tariff);
                userRepository.save(user);
                return ResponseEntity.ok("Tariff is set.");
            }
        }

        return ResponseEntity.badRequest().body("Tariff isn't set.");
    }
}
