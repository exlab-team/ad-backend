package com.exlab.incubator.exception;

public class TariffNameException extends RuntimeException {

    public TariffNameException(String noSuchTariffName) {
        super(noSuchTariffName);
    }
}
