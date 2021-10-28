package com.grin.lexicom.rest;

import com.grin.lexicom.model.User;
import com.grin.lexicom.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.web.context.WebApplicationContext;

import static com.grin.lexicom.LexicomApplicationTests.mapToJson;
import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ContextConfiguration
@AutoConfigureMockMvc
class AuthenticationRestControllerV1Test {
    @Autowired
    public WebApplicationContext context;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @Test
    void invalidRegistrationValidation() throws Exception {
        User user = new User();
        user.setUserName("ter!");
        user.setFirstName("");
        user.setSurName("");
        user.setLastName("");
        user.setPassword("");
        String inputJson = mapToJson(user);

        ResultActions resultActions = this.mockMvc.perform(
                post("/api/v1/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)
        );

        resultActions.andDo(MockMvcResultHandlers.print());
        resultActions.andExpect(status().is(400));
        resultActions
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.apierror.status", containsString("BAD_REQUEST")));
    }

    @Test
    void validRegistrationValidation() throws Exception {
        User user = new User();
        user.setUserName("testg");
        user.setFirstName("test");
        user.setSurName("test");
        user.setLastName("test");
        user.setPassword("Test12345^");
        user.setEmail("teddst@gmail.com");
        user.setPhone("+3809997999");
        String inputJson = mapToJson(user);

        when(userRepository.save(user)).thenReturn(user);

        this.mockMvc.perform(
                post("/api/v1/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(status().is(200));

    }
}
