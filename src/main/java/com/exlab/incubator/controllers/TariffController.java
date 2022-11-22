package com.exlab.incubator.controllers;

import com.exlab.incubator.configuration.jwt.JwtUtils;
import com.exlab.incubator.dto.requests.TariffRequest;
import com.exlab.incubator.services.interfaces.TariffPlanService;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties.Jwt;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tariff")
@CrossOrigin(origins = "*", maxAge = 3600)
public class TariffController {

    private TariffPlanService tariffPlanService;

    @Autowired
    public TariffController(TariffPlanService tariffPlanService) {
        this.tariffPlanService = tariffPlanService;
    }

    @PostMapping("/setting")
    public ResponseEntity<?> setTariff(@RequestBody TariffRequest tariffRequest, HttpServletRequest request){
        return tariffPlanService.setTariffPlanToTheUser(tariffRequest, request.getHeader("Authorization").substring(7));
    }

}
