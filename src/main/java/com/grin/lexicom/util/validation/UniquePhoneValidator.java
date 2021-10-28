package com.grin.lexicom.util.validation;

import com.grin.lexicom.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniquePhoneValidator implements ConstraintValidator<UniquePhone, String> {

    @Autowired
    private UserService userService;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (userService != null) {
            return value != null && !userService.phoneExists(value);
        }
        return true;
    }

}
