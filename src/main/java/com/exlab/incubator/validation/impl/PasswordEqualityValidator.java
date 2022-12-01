package com.exlab.incubator.validation.impl;

import com.exlab.incubator.validation.PasswordEquality;
import java.lang.reflect.Method;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


public class PasswordEqualityValidator implements ConstraintValidator<PasswordEquality, Object> {
    private String field;
    private String equalsTo;
    private String message = PasswordEquality.MESSAGE;

    public void initialize(PasswordEquality constraintAnnotation) {
        this.message = constraintAnnotation.message();
        this.field = constraintAnnotation.field();
        this.equalsTo = constraintAnnotation.equalsTo();
    }

    public boolean isValid(Object value, ConstraintValidatorContext context) {
        try {
            final Object fieldObject = getProperty(value, field, null);
            final Object equalsToObject = getProperty(value, equalsTo, null);

            if (fieldObject == null && equalsToObject == null) {
                return true;
            }

            boolean matches = (fieldObject != null)
                && fieldObject.equals(equalsToObject);

            if (!matches) {
                String msg = this.message;
                if( this.message == null
                    || "".equals( this.message )
                    || PasswordEquality.MESSAGE.equals( this.message ) ){
                    msg = field + " is not equal to " + equalsTo;
                }
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate( msg )
                    .addNode(equalsTo).addConstraintViolation();
            }

            return matches;
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    private Object getProperty(Object value, String fieldName,
        Object defaultValue) {
        Class<?> clazz = value.getClass();
        String methodName = "get" + Character.toUpperCase(fieldName.charAt(0))
            + fieldName.substring(1);
        try {
            Method method = clazz.getDeclaredMethod(methodName, new Class[0]);
            return method.invoke(value);
        } catch (Exception e) {
        }
        return defaultValue;
    }
}
