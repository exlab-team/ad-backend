package com.exlab.incubator.service;

import com.exlab.incubator.dto.requests.TariffDto;
import com.exlab.incubator.entity.Tariff;

public interface TariffService {

    Tariff createTariffIfNotExist(TariffDto tariffDto);

}
