package com.exlab.incubator.dto.requests;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.Value;

@Value
public class TariffDto {

   @Parameter(description = "one of the names: START, MIDDLE, PRO")
   String name;
   Double price;
}
