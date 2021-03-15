package com.haroldgao.projects.user.validator.bean.validation;

import com.haroldgao.projects.log.Logger;
import com.haroldgao.projects.user.domain.User;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

public class BeanValidatorDemo {
    public static void main(String[] args) {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();

        User user = new User();
        user.setPassword("123");
        user.setPhoneNumber("12345678");

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        violations.forEach(v -> {
            Logger.error(v.getMessage());
        });
    }
}
