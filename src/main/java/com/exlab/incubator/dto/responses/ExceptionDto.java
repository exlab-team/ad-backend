package com.exlab.incubator.dto.responses;

import lombok.Value;

@Value
public class ExceptionDto {

    String message;
    int statusCode;
    String statusMessage;
}
