package com.grin.lexicom.service.impl;

import com.grin.lexicom.model.User;
import com.grin.lexicom.repository.UserRepository;
import com.grin.lexicom.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.times;


@SpringBootTest
@ContextConfiguration
@AutoConfigureMockMvc
class UserServiceImplTest {

    private static final long USER_ID = 1L;
    private static final String USER_NAME = "Test";

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @Test
    void create() {
        User user = new User();
        user.setPassword(USER_NAME);
        when(userRepository.save(user)).thenReturn(user);

        User returned = userService.create(user);

        verify(userRepository, atLeast(1)).save(user);
        verifyNoMoreInteractions(userRepository);

        assertEquals(user, returned);
    }

    @Test
    void findAll() {
        List<User> users = new ArrayList<>();
        when(userRepository.findAll()).thenReturn(users);

        List<User> returned = userService.findAll();

        verify(userRepository, times(1)).findAll();

        verifyNoMoreInteractions(userRepository);

        assertEquals(users, returned);
    }

    @Test
    void findByUsername() {
        User user = new User();
        when(userRepository.findByUserName(USER_NAME)).thenReturn(user);

        User returned = userService.findByUsername(USER_NAME);

        verify(userRepository, atLeast(1)).findByUserName(USER_NAME);
        verifyNoMoreInteractions(userRepository);

        assertEquals(user, returned);
    }

    @Test
    void findById() {
        User user = new User();
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));

        Optional<User> returned = userService.findById(USER_ID);

        verify(userRepository, atLeast(1)).findById(USER_ID);
        verifyNoMoreInteractions(userRepository);

        assertEquals(user, returned.orElse(null));
    }
}
