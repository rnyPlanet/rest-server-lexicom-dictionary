package com.grin.lexicom.rest;


import com.grin.lexicom.model.User;
import com.grin.lexicom.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/auth/")
@Slf4j
public class AuthenticationRestControllerV1 {

    private final UserService userService;

    @Autowired
    public AuthenticationRestControllerV1(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("signup")
    @ResponseBody
    public ResponseEntity<User> reg(@Valid @RequestBody User user) {

        User newUser = userService.create(user);

        return new ResponseEntity<>(newUser, HttpStatus.OK);
    }
}
