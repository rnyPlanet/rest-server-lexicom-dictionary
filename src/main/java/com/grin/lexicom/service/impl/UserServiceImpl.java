package com.grin.lexicom.service.impl;

import com.grin.lexicom.util.error.UserAlreadyExistException;
import com.grin.lexicom.model.Role;
import com.grin.lexicom.model.Status;
import com.grin.lexicom.model.User;
import com.grin.lexicom.repository.RoleRepository;
import com.grin.lexicom.repository.UserRepository;
import com.grin.lexicom.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository,
                           BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User create(User user) {
        Role roleUser = roleRepository.findByName("ROLE_USER");
        List<Role> userRoles = new ArrayList<>();
        userRoles.add(roleUser);

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(userRoles);
        user.setStatus(Status.ACTIVE);

        return userRepository.save(user);
    }

    @Override
    public boolean emailExists(final String email) {
        return userRepository.findByEmail(email) != null;
    }

    @Override
    public boolean usernameExists(final String username) {
        return userRepository.findByUserName(username) != null;
    }

    @Override
    public boolean phoneExists(String phone) {
        return userRepository.findByPhone(phone) != null;
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUserName(username);
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

}
