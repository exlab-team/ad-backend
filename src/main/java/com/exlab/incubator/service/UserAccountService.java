package com.exlab.incubator.service;

import com.exlab.incubator.entity.Tariff;

public interface UserAccountService {

    boolean setTariff(Tariff valueOf, String authorization);
}

