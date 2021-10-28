package com.grin.lexicom.util.validation;

import com.google.common.base.Joiner;
import org.passay.*;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;

public class UsernameConstraintValidator implements ConstraintValidator<ValidUsername, String> {
    @Override
    public void initialize(final ValidUsername arg0) {

    }

    @Override
    public boolean isValid(String username, final ConstraintValidatorContext context) {
        String checked = username == null ? username = "" : username;

        final PasswordValidator validator = new PasswordValidator(Arrays.asList(
                new AllowedRegexRule("^[A-Za-z0-9_-]*$"),
                new LengthRule(3, 30),
                new WhitespaceRule()));
        final RuleResult result = validator.validate(new PasswordData(checked));
        if (result.isValid()) {
            return true;
        }
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(Joiner.on(",").join(validator.getMessages(result)).replace("Password", "Username")).addConstraintViolation();
        return false;
    }

}
