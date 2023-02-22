package com.exlab.incubator.controller;

import com.exlab.incubator.dto.requests.TariffDto;
import com.exlab.incubator.entity.Tariff;
import com.exlab.incubator.service.TariffService;
import com.exlab.incubator.service.UserAccountService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/accounts")
public class UserAccountController {

    private final UserAccountService userAccountService;
    private final TariffService tariffService;

    @Autowired
    public UserAccountController(UserAccountService userAccountService, TariffService tariffService) {
        this.userAccountService = userAccountService;
        this.tariffService = tariffService;
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/tariffs")
    public void getAllTariffPlans(){}

    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasRole('USER')")
    @PutMapping("/{id}/tariffs")
    public ResponseEntity<?> updateUserAccountWithTariff(@PathVariable Long id, @RequestBody TariffDto tariffDto){
        Tariff tariff = tariffService.createTariffIfNotExist(tariffDto);
        return userAccountService.selectTariff(id, tariff)
            ? ResponseEntity.ok().build()
            : ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
    }

/*    @PreAuthorize("hasRole('USER')")
    @PutMapping("/tariffs/{tariff_name}")
    public ResponseEntity<?> setTariffPlanToTheUserAccount(@PathVariable String tariff_name, HttpServletRequest request){
        return userAccountService.setTariff(Tariff.valueOf(tariff_name.toUpperCase()),
                                            request.getHeader("Authorization").substring(7))
            ? noContent().build()
            : notFound().build();
    }*/



}
