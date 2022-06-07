package com.example.simplelogin.utils;

import org.springframework.stereotype.Component;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.Set;

@Component
public class ValidationUtil {

    public Validator validator() {
        return new LocalValidatorFactoryBean();
    }

    public <T> void validate(T any) {
        Set<ConstraintViolation<T>> result = validator().validate(any);
        if (result.size() != 0) {
            throw new ConstraintViolationException(result);
        }
    }
}
