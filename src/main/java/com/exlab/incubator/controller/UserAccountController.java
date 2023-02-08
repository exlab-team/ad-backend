package com.exlab.incubator.controller;

import static org.springframework.http.ResponseEntity.noContent;
import static org.springframework.http.ResponseEntity.notFound;

import com.exlab.incubator.entity.Tariff;
import com.exlab.incubator.service.UserAccountService;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/accounts")
public class UserAccountController {

    private final UserAccountService userAccountService;

    @Autowired
    public UserAccountController(UserAccountService userAccountService) {
        this.userAccountService = userAccountService;
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/tariffs")
    public void getAllTariffPlans(){}

    @PreAuthorize("hasRole('USER')")
    @PutMapping("/tariffs/{tariff_name}")
    public ResponseEntity<?> setTariffPlanToTheUserAccount(@PathVariable String tariff_name, HttpServletRequest request){
        return userAccountService.setTariff(Tariff.valueOf(tariff_name.toUpperCase()),
                                            request.getHeader("Authorization").substring(7))
            ? noContent().build()
            : notFound().build();
    }



}
