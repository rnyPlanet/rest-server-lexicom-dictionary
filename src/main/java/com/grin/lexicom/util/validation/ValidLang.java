package com.grin.lexicom.util.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({TYPE, FIELD, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = LangValidator.class)
@Documented
public @interface ValidLang {

    String message() default "Invalid language";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
