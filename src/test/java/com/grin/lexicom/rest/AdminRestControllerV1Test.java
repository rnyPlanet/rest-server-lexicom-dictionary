package com.grin.lexicom.rest;

import com.grin.lexicom.model.User;
import com.grin.lexicom.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.grin.lexicom.LexicomApplicationTests.mapToJson;
import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ContextConfiguration
@AutoConfigureMockMvc
class AdminRestControllerV1Test {

    private static final long USER_ID = 1L;

    @Autowired
    private WebApplicationContext webapp;

    @Autowired
    private OAuthHelper authHelper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void setup() {
        user = new User();
        user.setId(USER_ID);
        user.setUserName("test");
        user.setFirstName("test");
        user.setSurName("test");
        user.setLastName("test");
        user.setPassword("Test912345^");
        user.setEmail("te9st@gmail.com");
        user.setPhone("+38099999999");
    }

//    @Test
//    void testAnonymous() throws Exception {
//        ResultActions resultActions = mockMvc.perform(get("/api/v1/admin/users/1")).andDo(print());
//
//        resultActions
//                .andExpect(status().isUnauthorized())
//                .andExpect(jsonPath("$.error", containsString("unauthorized")));
//    }

    @Test
    void testAuthenticatedAccessDenied() throws Exception {
        RequestPostProcessor bearerToken = authHelper.addBearerToken("test", "ROLE_USER");
        ResultActions resultActions = mockMvc.perform(get("/api/v1/admin/users/1").with(bearerToken)).andDo(print());

        resultActions
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.error", containsString("access_denied")));
    }

    @Test
    void getUserId() throws Exception {
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));

        RequestPostProcessor bearerToken = authHelper.addBearerToken("test", "ROLE_ADMIN");
        ResultActions resultActions = mockMvc.perform(get("/api/v1/admin/users/" + USER_ID).with(bearerToken)).andDo(print());

        String inputJson = mapToJson(user);
        resultActions
                .andExpect(status().isOk())
                .andExpect(content().string(inputJson));
    }

    @Test
    void getAllUsers() throws Exception {
        List<User> users = new ArrayList<User>(Collections.singleton(user));
        when(userRepository.findAll()).thenReturn(users);

        RequestPostProcessor bearerToken = authHelper.addBearerToken("test", "ROLE_ADMIN");
        ResultActions resultActions = mockMvc.perform(get("/api/v1/admin/users/all").with(bearerToken)).andDo(print());

        String inputJson = mapToJson(user);

        resultActions
                .andExpect(status().isOk())
                .andExpect(content().string("[" + inputJson + "]"));
    }

}
