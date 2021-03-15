package com.haroldgao.projects.user.validator.bean.validation;

import com.haroldgao.projects.user.domain.User;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UserValidConstraintValidator implements ConstraintValidator<UserValid, User> {
    @Override
    public void initialize(UserValid constraintAnnotation) {

    }

    @Override
    public boolean isValid(User value, ConstraintValidatorContext context) {
        return false;
    }
}
