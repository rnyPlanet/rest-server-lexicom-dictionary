package com.grin.lexicom.rest;

import com.grin.lexicom.model.User;
import com.grin.lexicom.repository.UserRepository;
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

import static com.grin.lexicom.LexicomApplicationTests.mapToJson;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ContextConfiguration
@AutoConfigureMockMvc
class UserRestControllerV1Test {

//    @MockBean
//    private UserRepository userRepository;
//
//    private User user;
//    private static final long USER_ID = 1L;
//    private static final String USERNAME = "test";
//
//    @Autowired
//    public WebApplicationContext context;
//
//    @Autowired
//    private OAuthHelper authHelper;
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Test
//    void getThisUser() throws Exception {
//        User user = new User();
//        user.setUserName(USERNAME);
//        user.setFirstName("test");
//        user.setSurName("test");
//        user.setLastName("test");
//        user.setPassword("Test12345^");
//        user.setEmail("teddst@gmail.com");
//        user.setPhone("+3809997999");
//        when(userRepository.findByUserName(USERNAME)).thenReturn(user);
//
//        RequestPostProcessor bearerToken = authHelper.addBearerToken(USERNAME, "ROLE_USER");
//        ResultActions resultActions = mockMvc.perform(get("/api/v1/users/me").with(bearerToken)).andDo(print());
//
//        String inputJson = mapToJson(user);
//        resultActions
//                .andExpect(status().isOk())
//                .andExpect(content().string(inputJson));
//    }
//
//    @Test
//    void getThisUserNotFound() throws Exception {
//        User user = new User();
//        user.setUserName(USERNAME);
//        user.setFirstName("test");
//        user.setSurName("test");
//        user.setLastName("test");
//        user.setPassword("Test12345^");
//        user.setEmail("teddst@gmail.com");
//        user.setPhone("+3809997999");
//        when(userRepository.findByUserName(USERNAME+"1")).thenReturn(user);
//
//        RequestPostProcessor bearerToken = authHelper.addBearerToken(USERNAME, "ROLE_USER");
//        ResultActions resultActions = mockMvc.perform(get("/api/v1/users/me").with(bearerToken)).andDo(print());
//
//        resultActions
//                .andExpect(status().isNotFound());
//    }
}
