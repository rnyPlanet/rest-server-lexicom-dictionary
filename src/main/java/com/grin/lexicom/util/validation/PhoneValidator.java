package com.grin.lexicom.util.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class PhoneValidator implements ConstraintValidator<ValidPhone, String> {
    private static final String EMAIL_PATTERN = "^\\+(?:[0-9]?){6,14}[0-9]$";
    private static final Pattern PATTERN = Pattern.compile(EMAIL_PATTERN);

    @Override
    public boolean isValid(final String phone, final ConstraintValidatorContext context) {
        return (validatePhone(phone));
    }

    private boolean validatePhone(String phone) {
        String checked = phone == null ? phone = "" : phone;

        Matcher matcher = PATTERN.matcher(checked);
        return matcher.matches();
    }
}
