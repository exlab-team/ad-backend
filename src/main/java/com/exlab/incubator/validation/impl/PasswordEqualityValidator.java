package com.exlab.incubator.validation.impl;

import com.exlab.incubator.validation.PasswordEquality;
import java.lang.reflect.Method;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PasswordEqualityValidator implements ConstraintValidator<PasswordEquality, Object> {
    private String field;
    private String equalsTo;
    private String message = "fields.notMatches";

    public void initialize(PasswordEquality constraintAnnotation) {
        this.message = constraintAnnotation.message();
        this.field = constraintAnnotation.field();
        this.equalsTo = constraintAnnotation.equalsTo();
    }

    public boolean isValid(Object value, ConstraintValidatorContext context) {
        try {
            Object password = getProperty(value, field, null);
            Object confirmPassword = getProperty(value, equalsTo, null);

            if (password == null && confirmPassword == null) return true;

            boolean matches = (password != null) && password.equals(confirmPassword);

            if (!matches) {
                String constraintMessage = this.message;
                if(this.message == null || this.message.equals("") || this.message.equals("fields.notMatches")){
                    constraintMessage = field + " is not equal to " + equalsTo;
                }
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(constraintMessage).addNode(equalsTo).addConstraintViolation();
            }

            return matches;
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }

    }

    private Object getProperty(Object value, String fieldName, Object defaultValue) {
        Class<?> clazz = value.getClass();
        String methodName = "get" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
        try {
            Method method = clazz.getDeclaredMethod(methodName, new Class[0]);
            return method.invoke(value);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return defaultValue;
    }
}
