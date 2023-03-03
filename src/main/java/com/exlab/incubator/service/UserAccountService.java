package com.exlab.incubator.service;

import com.exlab.incubator.entity.Tariff;

public interface UserAccountService {

    boolean establishTariffToUser(Long account_id, Tariff tariff);
}

