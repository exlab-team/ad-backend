package com.exlab.incubator.dto.requests;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Value;

@Value
public class TariffDto {

   @Schema(description = "one of the names: start, middle, pro")
   String name;
   Double price;
}
