package com.exlab.incubator.service;

import com.exlab.incubator.dto.requests.TariffDto;
import com.exlab.incubator.entity.Tariff;

public interface UserAccountService {

    boolean selectTariff(Long account_id, Tariff tariff);
}

