package com.exlab.incubator.service;

import com.exlab.incubator.dto.requests.TariffRequest;
import org.springframework.http.ResponseEntity;

public interface TariffPlanService {

    ResponseEntity<?> setTariffPlanToTheUser(TariffRequest tariffRequest, String jwt);

}
