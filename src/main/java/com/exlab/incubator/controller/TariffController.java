package com.exlab.incubator.controller;

import com.exlab.incubator.dto.requests.TariffRequest;
import com.exlab.incubator.service.TariffPlanService;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
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
