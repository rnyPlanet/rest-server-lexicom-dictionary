package com.grin.lexicom.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.grin.lexicom.model.Dictionary;
import com.grin.lexicom.model.Language;
import com.grin.lexicom.model.User;
import com.grin.lexicom.repository.DictionaryRepository;
import com.grin.lexicom.repository.UserRepository;
import com.grin.lexicom.service.DictionaryService;
import com.grin.lexicom.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ContextConfiguration
@AutoConfigureMockMvc
class DictionaryRestControllerV1Test {

    private static final long USER_ID = 1L;
    private static final String USERNAME = "test";

    @Autowired
    private WebApplicationContext webapp;

    @Autowired
    private OAuthHelper authHelper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private UserService userService;

    @MockBean
    private DictionaryRepository dictionaryRepository;

    @MockBean
    private DictionaryService dictionaryService;

    private User user;
    private Dictionary dictionary;

    @BeforeEach
    void setup() {
        user = new User();
        user.setId(USER_ID);
        user.setUserName(USERNAME);
        user.setFirstName("test");
        user.setSurName("test");
        user.setLastName("test");
        user.setPassword("Test912345^");
        user.setEmail("te9st@gmail.com");
        user.setPhone("+38099999999");

        Language language = new Language();
        language.setId(1L);
        language.setLang("English");

        dictionary = new Dictionary();
        dictionary.setName(USERNAME);
        dictionary.setLang(language);
        dictionary.setDescription("Test");
        dictionary.setPrivate(true);
    }

    @Test
    void findMy() throws Exception {
        when(userService.findByUsername(USERNAME)).thenReturn(user);

        List<Dictionary> dictionaries = new ArrayList<>(Collections.singleton(dictionary));
        when(dictionaryService.findMy(user.getId())).thenReturn(dictionaries);

        RequestPostProcessor bearerToken = authHelper.addBearerToken(USERNAME, "ROLE_USER");
        ResultActions resultActions = mockMvc.perform(get("/api/v1/dictionaries/my").with(bearerToken)).andDo(print());

        String inputJson = mapToJson(dictionary);
        resultActions
                .andExpect(status().isOk())
                .andExpect(content().string("[" + inputJson + "]"));
    }

    @Test
    void findAllByPrivateIsNot() throws Exception {
        dictionary.setPrivate(false);
        dictionary.setUser(user);
        when(dictionaryService.findAllByPrivateIsNot()).thenReturn(new ArrayList<>(Collections.singleton(dictionary)));
        String inputJson = mapToJson(dictionary);

        RequestPostProcessor bearerToken = authHelper.addBearerToken(USERNAME, "ROLE_USER");
        ResultActions resultActions = mockMvc.perform(get("/api/v1/dictionaries/").with(bearerToken))
                .andDo(print());
        resultActions
                .andExpect(status().isOk())
                .andExpect(content().string("[" + inputJson + "]"));
    }

    @Test
    void create() throws Exception {
        when(userService.findByUsername(USERNAME)).thenReturn(user);
        when(dictionaryRepository.save(dictionary)).thenReturn(dictionary);

        String inputJson = mapToJson(dictionary);

        RequestPostProcessor bearerToken = authHelper.addBearerToken(USERNAME, "ROLE_USER");
        ResultActions resultActions = mockMvc.perform(post("/api/v1/dictionaries/create")
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson).with(bearerToken))
                .andDo(print());
        resultActions
                .andExpect(status().isOk());
    }

    @Test
    void createDoesntExists() throws Exception {
        Dictionary dictionaryA = new Dictionary();
        dictionaryA.setName(USERNAME + "1");

        Language language = new Language();
        language.setId(1L);
        language.setLang("English");

        dictionaryA.setLang(language);
        dictionaryA.setDescription("Test");
        dictionaryA.setPrivate(true);

        when(userService.findByUsername(USERNAME)).thenReturn(user);
        when(dictionaryService.findByNameAndUserId(dictionary.getName(), user.getId())).thenReturn(new ArrayList<>(Collections.singleton(dictionaryA)));
        when(dictionaryRepository.save(dictionary)).thenReturn(dictionary);

        String inputJson = mapToJson(dictionary);

        RequestPostProcessor bearerToken = authHelper.addBearerToken(USERNAME, "ROLE_USER");
        ResultActions resultActions = mockMvc.perform(post("/api/v1/dictionaries/create")
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson).with(bearerToken))
                .andDo(print());
        resultActions
                .andExpect(status().isOk());
    }

    @Test
    void createAlredyExists() throws Exception {
        dictionary.setUser(user);
        when(userService.findByUsername(USERNAME)).thenReturn(user);
        when(dictionaryRepository.save(dictionary)).thenReturn(dictionary);
        when(dictionaryService.findByNameAndUserId(dictionary.getName(), user.getId())).thenReturn(new ArrayList<>(Collections.singleton(dictionary)));

        String inputJson = mapToJson(dictionary);

        RequestPostProcessor bearerToken = authHelper.addBearerToken(USERNAME, "ROLE_USER");
        ResultActions resultActions = mockMvc.perform(post("/api/v1/dictionaries/create")
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson).with(bearerToken))
                .andDo(print());
        resultActions
                .andExpect(status().isBadRequest());
    }

    @Test
    void createInvalid() throws Exception {
        dictionary.setDescription("");
        when(dictionaryRepository.save(dictionary)).thenReturn(dictionary);

        String inputJson = mapToJson(dictionary);

        RequestPostProcessor bearerToken = authHelper.addBearerToken(USERNAME, "ROLE_USER");
        ResultActions resultActions = mockMvc.perform(post("/api/v1/dictionaries/create")
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson).with(bearerToken))
                .andDo(print());
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.apierror.status", containsString("BAD_REQUEST")));
    }
}
