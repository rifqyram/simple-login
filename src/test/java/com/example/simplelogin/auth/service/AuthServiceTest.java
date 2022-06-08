package com.example.simplelogin.auth.service;

import com.example.simplelogin.auth.entity.User;
import com.example.simplelogin.auth.entity.UserDetailImpl;
import com.example.simplelogin.auth.model.RefreshTokenResponse;
import com.example.simplelogin.auth.model.SignInResponse;
import com.example.simplelogin.auth.model.UserRequest;
import com.example.simplelogin.auth.model.UserResponse;
import com.example.simplelogin.auth.repository.UserRepository;
import com.example.simplelogin.auth.service.impl.AuthServiceImpl;
import com.example.simplelogin.constant.ResponseMessage;
import com.example.simplelogin.security.jwt.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;
    @MockBean
    private AuthenticationManager authenticationManager;
    @MockBean
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtUtils jwtUtils;
    @Mock
    private RefreshTokenService refreshTokenService;
    @MockBean
    private Authentication authentication;

    private AuthService authService;
    private UserRequest userRequest;

    @BeforeEach
    void setUp() {
        userRequest = new UserRequest("test@gmail.com", "password");
        authService = new AuthServiceImpl(userRepository, authenticationManager, passwordEncoder, jwtUtils, refreshTokenService);
    }

    @Test
    void testSignUpAndCreateUserSuccessfully() {
        User user = new User("1", "test@mail.com", "password");
        when(userRepository.save(any(User.class))).thenReturn(user);
        UserResponse response = authService.signUp(userRequest);
        assertEquals(user.getEmail(), response.getEmail());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void itShouldSignAndReturnSignInResponseSuccessfully() {
        UserDetailImpl userDetail = new UserDetailImpl("1", "test@mail.com", "password");
        when(authenticationManager.authenticate(any(Authentication.class))).thenReturn(authentication);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetail);
        when(jwtUtils.generateJwtToken(userDetail)).thenReturn("token");

        RefreshTokenResponse refreshTokenResponse = new RefreshTokenResponse(
                "1",
                new UserResponse(userDetail.getUsername()),
                "token",
                System.currentTimeMillis() + 1800_000
        );

        when(refreshTokenService.create(userDetail.getId())).thenReturn(refreshTokenResponse);

        SignInResponse signInResponse = authService.signIn(userRequest);
        assertEquals("token", signInResponse.getAccessToken());
        assertEquals(refreshTokenResponse.getToken(), signInResponse.getRefreshToken());
        assertEquals(userDetail.getUsername(), signInResponse.getEmail());
    }

    @Test
    void itShouldDeleteRefreshTokenWhenUserLogout() {
        UserDetailImpl userDetail = new UserDetailImpl("1", "test@mail.com", "password");
        when(authentication.getPrincipal()).thenReturn(userDetail);
        String logout = authService.logout(authentication);
        verify(refreshTokenService, times(1)).delete(userDetail.getId());
        assertEquals(ResponseMessage.LOGOUT_SUCCESS, logout);
    }
}