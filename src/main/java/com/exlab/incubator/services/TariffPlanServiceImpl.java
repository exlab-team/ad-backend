package com.exlab.incubator.services;

import com.exlab.incubator.configuration.jwt.JwtUtils;
import com.exlab.incubator.dto.requests.TariffRequest;
import com.exlab.incubator.entities.Tariff;
import com.exlab.incubator.entities.User;
import com.exlab.incubator.repositories.UserRepository;
import com.exlab.incubator.services.interfaces.TariffPlanService;
import com.exlab.incubator.services.interfaces.UserService;
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
