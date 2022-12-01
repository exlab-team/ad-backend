package com.exlab.incubator.validation;

import com.exlab.incubator.validation.impl.PasswordEqualityValidator;
import javax.validation.Constraint;
import javax.validation.Payload;



import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(TYPE)
@Retention(RUNTIME)
@Constraint(validatedBy = PasswordEqualityValidator.class)
@Documented
public @interface PasswordEquality {

    String message() default "fields.notMatches";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @Target(TYPE)
    @Retention(RUNTIME)
    @Documented
    @interface List {
        PasswordEquality[] value();
    }

    String field();

    String equalsTo();
}