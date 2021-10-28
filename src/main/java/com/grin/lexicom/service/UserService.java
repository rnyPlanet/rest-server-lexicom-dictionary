package com.grin.lexicom.service;

import com.grin.lexicom.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User create(User user);

    List<User> findAll();

    User findByUsername(String username);

    Optional<User> findById(Long id);

    boolean emailExists(final String email);

    boolean usernameExists(final String username);

    boolean phoneExists(final String phone);
}
