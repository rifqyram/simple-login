package com.example.simplelogin.auth.controller;

import com.example.simplelogin.auth.model.UserResponse;
import com.example.simplelogin.auth.service.UserService;
import com.example.simplelogin.utils.WebResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class HelloControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    UserService userService;

    @Test
    @WithMockUser(username = "test@mail.com")
    void itShouldSendUserResponseAndReturnResponseOk() throws Exception {
        UserResponse userResponse = new UserResponse("test@mail.com");
        when(userService.getWithAuth(any(Authentication.class))).thenReturn(userResponse);

        WebResponse<?> webResponse = new WebResponse<>(
                HttpStatus.OK.value(),
                HttpStatus.OK,
                "Success",
                userResponse
        );

        mockMvc.perform(get("/api/v1/hello")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(webResponse)));
        verify(userService, times(1)).getWithAuth(any(Authentication.class));
    }
}