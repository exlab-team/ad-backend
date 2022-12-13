package com.exlab.incubator.dto.responses;

import lombok.Value;

@Value
public class ExceptionDto {

    private String message;
    private int statusCode;
    private String statusMessage;
}
