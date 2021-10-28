package com.grin.lexicom.util.validation;
import javax.validation.*;

import com.grin.lexicom.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {

    @Autowired
    private UserService userService;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (userService != null) {
            return value != null && !userService.emailExists(value);
        }
        return true;
    }

}
