package com.grin.lexicom.rest;

import com.grin.lexicom.model.Dictionary;
import com.grin.lexicom.model.Language;
import com.grin.lexicom.model.User;
import com.grin.lexicom.model.Word;
import com.grin.lexicom.service.DictionaryService;
import com.grin.lexicom.service.LanguageService;
import com.grin.lexicom.service.UserService;
import com.grin.lexicom.service.WordService;
import com.grin.lexicom.util.error.ApiError;
import com.grin.lexicom.util.error.ApiValidationError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/api/v1/language/")
public class LanguageRestControllerV1 {

    private final LanguageService languageService;

    @Autowired
    public LanguageRestControllerV1(@Lazy LanguageService languageService) {
        this.languageService = languageService;
    }

    @GetMapping(value = "")
    public ResponseEntity<List<Language>> findAllByPrivateIsNot() {
        return new ResponseEntity<>(languageService.findAll(), HttpStatus.OK);
    }

}
