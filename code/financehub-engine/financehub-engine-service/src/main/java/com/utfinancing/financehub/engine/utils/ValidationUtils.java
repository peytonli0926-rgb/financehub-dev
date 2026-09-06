package com.utfinancing.financehub.engine.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class ValidationUtils implements ApplicationContextAware {

    private static List<Validator> validator = new ArrayList<>();


    public static void validate(Object object) {
        for (Validator validator : validator) {
            Set<ConstraintViolation<Object>> validate = validator.validate(object);
            if (!validate.isEmpty()) {
                throw new ConstraintViolationException(validate);
            }
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        validator = new ArrayList<>(applicationContext.getBeansOfType(Validator.class).values());
    }
}