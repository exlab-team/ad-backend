package com.exlab.incubator.validation.impl;

import com.exlab.incubator.validation.PasswordEquality;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.springframework.beans.BeanWrapperImpl;


public class PasswordEqualityValidator implements ConstraintValidator<PasswordEquality, Object> {

    private String field;
    private String equalsTo;

    public void initialize(PasswordEquality constraintAnnotation) {
        this.field = constraintAnnotation.field();
        this.equalsTo = constraintAnnotation.equalsTo();
    }

    public boolean isValid(Object value, ConstraintValidatorContext context) {
        Object password = new BeanWrapperImpl(value).getPropertyValue(field);
        Object confirmPassword = new BeanWrapperImpl(value).getPropertyValue(equalsTo);

        if (password != null) {
            return password.equals(confirmPassword);
        } else {
            return confirmPassword == null;
        }
    }
}
