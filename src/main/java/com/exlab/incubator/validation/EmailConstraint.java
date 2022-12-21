package com.exlab.incubator.validation;

import java.lang.annotation.*;
import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Constraint(validatedBy = EmailValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface EmailConstraint {
    String message() default "It allows numeric values from 0 to 9.\n" +
            "Both uppercase and lowercase letters from a to z are allowed.\n" +
            "Allowed are underscore “_”, hyphen “-“, and dot “.”\n" +
            "Dot isn't allowed at the start and end of the local part.\n" +
            "Consecutive dots aren't allowed.\n" +
            "For the local part, a maximum of 64 characters are allowed.\n" +
            "It allows numeric values from 0 to 9.\n" +
            "We allow both uppercase and lowercase letters from a to z.\n" +
            "Hyphen “-” and dot “.” aren't allowed at the start and end of the domain part.\n" +
            "No consecutive dots." ;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
