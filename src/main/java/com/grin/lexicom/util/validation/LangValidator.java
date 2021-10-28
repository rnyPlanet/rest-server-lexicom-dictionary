package com.grin.lexicom.util.validation;

import com.grin.lexicom.model.Dictionary;
import com.grin.lexicom.model.Language;
import com.grin.lexicom.service.LanguageService;
import com.grin.lexicom.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class LangValidator implements ConstraintValidator<ValidLang, Language> {
    @Autowired
    private LanguageService languageService;

    @Override
    public boolean isValid(final Language lang, final ConstraintValidatorContext context) {
        if (languageService != null) {
            return lang != null && languageService.findById(lang.getId()).isPresent();
        }

        return true;
    }

}
