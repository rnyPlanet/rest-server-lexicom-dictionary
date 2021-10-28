package com.grin.lexicom.rest;

import com.grin.lexicom.dto.UserSubscribeDictionaryDto;
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
import java.util.*;

@RestController
@RequestMapping(value = "/api/v1/dictionaries/")
public class DictionaryRestControllerV1 {

    private final DictionaryService dictionaryService;
    private final WordService wordService;
    private final UserService userService;

    @Autowired
    public DictionaryRestControllerV1(@Lazy DictionaryService dictionaryService, UserService userService, WordService wordService) {
        this.dictionaryService = dictionaryService;
        this.userService = userService;
        this.wordService = wordService;
    }

    @GetMapping(value = "my")
    public ResponseEntity<List<Dictionary>> findMy(Principal principal) {
        User result = userService.findByUsername(principal.getName());

        return new ResponseEntity<>(dictionaryService.findMy(result.getId()), HttpStatus.OK);
    }

    private ResponseEntity<?> findNotPrivateDict(Long userId, Long dictId) {
        Dictionary dictionary = dictionaryService.findByUserIdAndDictId(userId, dictId);
        List<Dictionary> dictionaries = dictionaryService.findAllByPrivateIsNot();

        if (dictionary == null) {
            for (Dictionary dict : dictionaries) {
                if (dict.getId().equals(dictId)) {
                    return new ResponseEntity<>(dict, HttpStatus.OK);
                }
            }

            ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, "Access error");
            apiError.addSubError(new ApiValidationError("Dictionary", "id", dictId, "No such dictionary exists or you do not have access!"));
            return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(dictionary, HttpStatus.OK);
    }

    @GetMapping(value = "{id}")
    public ResponseEntity<?> findById(@PathVariable(name = "id") Long id,
                                      Principal principal,
                                      @RequestParam(required = false) String pass) {
        User user = userService.findByUsername(principal.getName());
        Dictionary dictionary = dictionaryService.findById(id).orElse(null);

        if (dictionary == null) {
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }

        if ( user.getSubscribedOn().contains(dictionary)) {
            return new ResponseEntity<>(dictionary, HttpStatus.OK);
        }

        if (dictionary.isPrivate() &&  dictionary.getPass().equals(pass)) {
            return new ResponseEntity<>(dictionary, HttpStatus.OK);
        }

        return findNotPrivateDict(userService.findByUsername(principal.getName()).getId(), id);
    }

    @GetMapping(value = "{id}/words")
    public ResponseEntity<?> findAllWordsByDictionaryId(@PathVariable(name = "id") Long id,
                                                        Principal principal,
                                                        @RequestParam(required = false) String pass) {
        User user = userService.findByUsername(principal.getName());
        Dictionary dictionary = dictionaryService.findById(id).orElse(null);

        if (dictionary == null) {
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }

        if (user.getSubscribedOn().contains(dictionary)) {
            return new ResponseEntity<>(dictionary.getWords(), HttpStatus.OK);
        }

        if (dictionary.isPrivate() &&  dictionary.getPass().equals(pass)) {
            return new ResponseEntity<>(dictionary.getWords(), HttpStatus.OK);
        }

        ResponseEntity<?> response = findNotPrivateDict(userService.findByUsername(principal.getName()).getId(), id);
        if (response.getStatusCode() != HttpStatus.OK) {
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }

        return new ResponseEntity<>(wordService.findAllByDictionaryId(id), HttpStatus.OK);
    }

    @PostMapping(value = "{id}/add")
    public ResponseEntity<?> addWord(@PathVariable(name = "id") Long id, @Valid @RequestBody Word word, Principal principal) {

        List<Dictionary> dictionaries = dictionaryService.findByUserId(userService.findByUsername(principal.getName()).getId());

        for (Dictionary dict : dictionaries) {
            if (dict.getId().equals(id)) {
                Optional<Dictionary> dictionary = dictionaryService.findById(id);
                if (dictionary.isPresent()) {
                    word.setDictionary(dictionary.get());
                    wordService.save(word);
                    return new ResponseEntity<>(wordService.findAllByDictionaryId(id), HttpStatus.OK);
                } else {
                    ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, "Access error");
                    apiError.addSubError(new ApiValidationError("Dictionary", "id", id, "No such dictionary exists or you do not have access!"));
                    return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
                }
            }
        }


        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, "Access error");
        apiError.addSubError(new ApiValidationError("Dictionary", "id", id, "No such dictionary exists or you do not have access!"));
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @GetMapping(value = "")
    public ResponseEntity<List<Dictionary>> findAllByPrivateIsNot() {
        return new ResponseEntity<>(dictionaryService.findAllByPrivateIsNot(), HttpStatus.OK);
    }

    @PostMapping("create")
    public ResponseEntity<?> create(@Valid @RequestBody Dictionary dictionary, Principal principal) {
        List<Dictionary> dictionaries = dictionaryService.findByNameAndUserId(dictionary.getName(), userService.findByUsername(principal.getName()).getId());

        for (Dictionary dict : dictionaries) {
            if (dict.getName().equals(dictionary.getName())) {
                ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, "Validation error");
                apiError.addSubError(new ApiValidationError("Dictionary", "name", dictionary.getName(), "You already have a dictionary with this name!"));
                return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
            }
        }

        dictionary.setUser(userService.findByUsername(principal.getName()));

        return new ResponseEntity<>(dictionaryService.create(dictionary), HttpStatus.OK);
    }

    @PostMapping("{id}/delete")
    public ResponseEntity<?> delete(@PathVariable(name = "id") Long id, Principal principal) {
        Dictionary dictionary = dictionaryService.findByUserIdAndDictId(userService.findByUsername(principal.getName()).getId(), id);

        if (dictionary == null) {
            ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, "Validation error");
            apiError.addSubError(new ApiValidationError("Dictionary", "id", id, "Нou don't have such a dictionary!"));
            return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
        }

        try {
            dictionaryService.delete(id);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("update")
    public ResponseEntity<?> update(@Valid @RequestBody Dictionary dictionary, Principal principal) {
        List<Dictionary> dictionaries = dictionaryService.findByUserId(userService.findByUsername(principal.getName()).getId());

        for (Dictionary dict : dictionaries) {
            if (dict.getName().equals(dictionary.getName())) {
                ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, "Validation error");
                apiError.addSubError(new ApiValidationError("Dictionary", "name", dictionary.getName(), "You already have a dictionary with this name!"));
                return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
            }
        }

        dictionary.setUser(userService.findByUsername(principal.getName()));

        return new ResponseEntity<>(dictionaryService.update(dictionary), HttpStatus.OK);
    }


    @GetMapping(value = "subscriptions")
    public ResponseEntity<List<Dictionary>> findMySubscriptions(Principal principal) {
        User user = userService.findByUsername(principal.getName());

        return new ResponseEntity<>(dictionaryService.findMySubscriptions(user.getId()), HttpStatus.OK);
    }

    @PostMapping(value = "{id}/subscribe")
    public ResponseEntity subscribe(Principal principal,
                                    @PathVariable(name = "id") Long idDictionary,
                                    @RequestParam(required = false) String pass) {
        User user = userService.findByUsername(principal.getName());
        Dictionary dictionary = dictionaryService.findById(idDictionary).orElse(null);

        if (dictionary == null) {
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }

        if (dictionary.isPrivate() &&  dictionary.getPass().equals(pass)) {
            if (isCanSubscribe(principal.getName(), idDictionary)) {
                dictionaryService.subscribe(idDictionary, user.getId());
                if (dictionaryService.isUserSubscribedById(idDictionary, user.getId())) {
                    return new ResponseEntity<>(HttpStatus.OK);
                }
            }
        } else if (!dictionary.isPrivate()) {
            if (isCanSubscribe(principal.getName(), idDictionary)) {
                dictionaryService.subscribe(idDictionary, user.getId());
                if (dictionaryService.isUserSubscribedById(idDictionary, user.getId())) {
                    return new ResponseEntity<>(HttpStatus.OK);
                }
            }
        }

        return new ResponseEntity(HttpStatus.FORBIDDEN);
    }

    private boolean isCanSubscribe(String username, Long idDictionary) {
        User user = userService.findByUsername(username);
        Dictionary dictionary = dictionaryService.findById(idDictionary).orElse(null);

        return dictionary != null && !user.getSubOn().contains(dictionary) && !user.getCreated().contains(dictionary);
    }

    @PutMapping(value = "{id}/unsubscribe")
    public ResponseEntity unsubscribe(Principal principal, @PathVariable(name = "id") Long idDictionary) {
        User user = userService.findByUsername(principal.getName());
        Dictionary dictionary = dictionaryService.findById(idDictionary).orElse(null);

        if (dictionary != null && user.getSubOn().contains(dictionary) && !user.getCreated().contains(dictionary)) {
            dictionaryService.unsubscribe(idDictionary, user.getId());
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping(value = "{id}/status")
    public ResponseEntity<Map<String, Boolean>> statusConsultation(Principal principal, @PathVariable(name = "id") Long idDictionary) {
        User user = userService.findByUsername(principal.getName());
//        Dictionary dictionary = dictionaryService.findMySubscriptionByDictIdAndUserId(idDictionary, user.getId());
        Dictionary dictionary = dictionaryService.findById(idDictionary).orElse(null);

        if (dictionary == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        Map<String, Boolean> statuses = new HashMap<>();

        if (user.getCreated().contains(dictionary)) {
            statuses.put("isCanManage", true);

            return new ResponseEntity<>(statuses, HttpStatus.OK);
        }

        statuses.put("isCanManage", false);
        statuses.put("isSubscribed", dictionaryService.isUserSubscribedById(dictionary.getId(), user.getId()));

        return new ResponseEntity<>(statuses, HttpStatus.OK);
    }


    @GetMapping(value = "{id}/users")
    public ResponseEntity<List<UserSubscribeDictionaryDto>> usersByDictId(Principal principal, @PathVariable(name = "id") Long idDictionary) {
        User user = userService.findByUsername(principal.getName());
        Dictionary dictionary = dictionaryService.findById(idDictionary).orElse(null);

        if (dictionary != null & user.getCreated().contains(dictionary)) {
            return new ResponseEntity<>(UserSubscribeDictionaryDto.fromUser(dictionary.getUsersCollection()), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @DeleteMapping(value = "{id}/users/{id_u}")
    public ResponseEntity deleteUserByDictIdAndUserId(Principal principal,
                                                      @PathVariable(name = "id") Long idDictionary,
                                                      @PathVariable(name = "id_u") Long idUser) {
        User user = userService.findByUsername(principal.getName());
        Dictionary dictionary = dictionaryService.findById(idDictionary).orElse(null);

        User deleteUser = userService.findById(idUser).orElse(null);

        if (dictionary != null && deleteUser != null &&
                user.getCreated().contains(dictionary) &
                dictionary.getUsersCollection().contains(deleteUser) &
                deleteUser.getSubOn().contains(dictionary)) {
            dictionaryService.unsubscribe(idDictionary, deleteUser.getId());
            return new ResponseEntity<>(HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }




}
