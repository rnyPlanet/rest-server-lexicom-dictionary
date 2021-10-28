package com.grin.lexicom.rest;

import com.grin.lexicom.model.Dictionary;
import com.grin.lexicom.model.User;
import com.grin.lexicom.model.Word;
import com.grin.lexicom.service.DictionaryService;
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
@RequestMapping(value = "/api/v1/word/")
public class WordRestControllerV1 {

    private final DictionaryService dictionaryService;
    private final WordService wordService;
    private final UserService userService;

    @Autowired
    public WordRestControllerV1(@Lazy DictionaryService dictionaryService, UserService userService, WordService wordService) {
        this.dictionaryService = dictionaryService;
        this.userService = userService;
        this.wordService = wordService;
    }

    @PostMapping("{id}/delete")
    public ResponseEntity<?> delete(@PathVariable(name = "id") Long id, Principal principal) {
        List<Dictionary> dictionaries = dictionaryService.findByUserId(userService.findByUsername(principal.getName()).getId());

        if (dictionaries == null && !dictionaries.isEmpty()) {
            ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, "Validation error");
            apiError.addSubError(new ApiValidationError("Word", "id", id, "Нou don't have such a word!"));
            return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
        }

        try {
            for (Dictionary dict : dictionaries) {
                if(wordService.findById(id).isPresent()) {
                    if (dict.getWords().contains(wordService.findById(id).get())) {
                        wordService.delete(id);
                        return new ResponseEntity<>(HttpStatus.OK);
                    }
                }
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PostMapping("update")
    public ResponseEntity<?> update(@Valid @RequestBody Word word, Principal principal) {
        List<Dictionary> dictionaries = dictionaryService.findByUserId(userService.findByUsername(principal.getName()).getId());

        for (Dictionary dict : dictionaries) {
            if(wordService.findById(word.getId()).isPresent()) {
                if (dict.getWords().contains(wordService.findById(word.getId()).get())) {
                    try {
                        word.setDictionary(dict);
                        wordService.save(word);
                    }  catch (Exception e) {
                        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
                    }

                    return new ResponseEntity<>(HttpStatus.OK);
                }
            }
        }

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
