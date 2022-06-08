package com.example.simplelogin.auth.controller;

import com.example.simplelogin.auth.model.RefreshTokenRequest;
import com.example.simplelogin.auth.model.SignInResponse;
import com.example.simplelogin.auth.model.UserRequest;
import com.example.simplelogin.auth.model.UserResponse;
import com.example.simplelogin.auth.service.AuthService;
import com.example.simplelogin.auth.service.RefreshTokenService;
import com.example.simplelogin.utils.ValidationUtil;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    @MockBean
    private RefreshTokenService refreshTokenService;

    @MockBean
    private Authentication authentication;

    @MockBean
    private ValidationUtil validationUtil;

    @Test
    @WithMockUser(username = "test@mail.com")
    void itShouldSignUpSuccessfullyAndReturnResponseCreated() throws Exception {
        UserResponse userResponse = new UserResponse("test@mail.com");
        when(authService.signUp(any(UserRequest.class))).thenReturn(userResponse);

        WebResponse<?> webResponse = new WebResponse<>(
                HttpStatus.CREATED.value(),
                HttpStatus.CREATED,
                "User created successfully",
                userResponse
        );

        mockMvc.perform(post("/api/v1/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userResponse)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(webResponse)));

        verify(validationUtil, times(1)).validate(any());
        verify(authService, times(1)).signUp(any(UserRequest.class));
    }

    @Test
    @WithMockUser(username = "test@mail.com")
    void itShouldSignInSuccessfullyAndReturnResponseOk() throws Exception {
        UserRequest request = new UserRequest("test@mail.com", "password");
        SignInResponse signInResponse = new SignInResponse("test@mail.com", "token", "token");
        when(authService.signIn(request)).thenReturn(signInResponse);

        WebResponse<?> webResponse = new WebResponse<>(
                HttpStatus.OK.value(),
                HttpStatus.OK,
                "User signed in successfully",
                signInResponse
        );

        mockMvc.perform(post("/api/v1/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(validationUtil, times(1)).validate(any());
        verify(authService, times(1)).signIn(any());
    }

    @Test
    @WithMockUser(username = "test@mail.com")
    void itShouldVerifyRefreshTokenAndReturnResponseOk() throws Exception {
        RefreshTokenRequest refreshTokenRequest = new RefreshTokenRequest("token");
        SignInResponse signInResponse = new SignInResponse("mail@gmail.com", "token", "token");
        when(refreshTokenService.verifyRefreshToken(refreshTokenRequest.getToken())).thenReturn(signInResponse);

        WebResponse<?> webResponse = new WebResponse<>(
                HttpStatus.OK.value(),
                HttpStatus.OK,
                "Token refreshed successfully",
                signInResponse
        );

        mockMvc.perform(post("/api/v1/auth/refresh-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(refreshTokenRequest)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(webResponse)));
    }

    @Test
    @WithMockUser(username = "test@mail.com")
    void itShouldLogoutSuccessfully() throws Exception {
        String message = "Logout Successfully";
        when(authService.logout(any())).thenReturn(message);

        WebResponse<?> webResponse = new WebResponse<>(
                HttpStatus.OK.value(),
                HttpStatus.OK,
                message,
                null
        );

        mockMvc.perform(post("/api/v1/auth/logout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(message)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(webResponse)));
    }
}