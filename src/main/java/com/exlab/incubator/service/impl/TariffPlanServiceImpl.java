package com.exlab.incubator.service.impl;

import com.exlab.incubator.configuration.jwt.JwtUtils;
import com.exlab.incubator.dto.requests.TariffDto;
import com.exlab.incubator.service.TariffPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class TariffPlanServiceImpl implements TariffPlanService {

    private JwtUtils jwtUtils;

    @Autowired
    public TariffPlanServiceImpl(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    public ResponseEntity<?> setTariffPlanToTheUser(TariffDto tariffDto, String jwt) {
      return ResponseEntity.ok("Default response");
    }
}
