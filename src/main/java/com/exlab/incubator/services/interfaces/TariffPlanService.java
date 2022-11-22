package com.exlab.incubator.services.interfaces;

import com.exlab.incubator.dto.requests.TariffRequest;
import org.springframework.http.ResponseEntity;

public interface TariffPlanService {

    ResponseEntity<?> setTariffPlanToTheUser(TariffRequest tariffRequest, String jwt);

}
