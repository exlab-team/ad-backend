package com.exlab.incubator.entity;

public enum TariffName {

    START,
    MIDDLE,
    PRO;

    public static TariffName of(String str) {
        for (TariffName tariff : TariffName.values()) {
            if (tariff.name().equalsIgnoreCase(str)) {
                return tariff;
            }
        }
        return null;
    }
}
