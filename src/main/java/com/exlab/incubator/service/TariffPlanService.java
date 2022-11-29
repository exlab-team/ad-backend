package com.exlab.incubator.service;

import com.exlab.incubator.dto.requests.TariffDto;
import org.springframework.http.ResponseEntity;

public interface TariffPlanService {

    ResponseEntity<?> setTariffPlanToTheUser(TariffDto tariffDto, String jwt);

}
