package com.haroldgao.projects.user.validator.bean.validation;

import com.haroldgao.projects.user.domain.User;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Constraint annotation for {@link User}.
 * Accept {@link User}.
 *
 * @since v4.1
 * @see Constraint
 */
@Documented
@Target(TYPE)
@Retention(RUNTIME)
@Constraint(validatedBy = UserValidConstraintValidator.class)
public @interface UserValid {

    String message() default "{com.haroldgao.projects.user.validator.bean.validation.UserValid.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
