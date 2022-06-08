package com.example.simplelogin.auth.service;

import com.example.simplelogin.auth.entity.RefreshToken;
import com.example.simplelogin.auth.entity.User;
import com.example.simplelogin.auth.model.RefreshTokenResponse;
import com.example.simplelogin.auth.repository.RefreshTokenRepository;
import com.example.simplelogin.auth.service.impl.RefreshTokenServiceImpl;
import com.example.simplelogin.exception.ExpiredTokenException;
import com.example.simplelogin.security.jwt.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class RefreshTokenServiceTest {

    private RefreshTokenService refreshTokenService;

    @Mock
    private UserService userService;

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @Mock
    private JwtUtils jwtUtils;

    User user;
    RefreshToken refreshToken;

    @BeforeEach
    void setUp() {
        user = new User("1", "test@gmail.com", "password");
        refreshToken = new RefreshToken(user, UUID.randomUUID().toString(), System.currentTimeMillis() + 1800000);
        refreshTokenService = new RefreshTokenServiceImpl(refreshTokenRepository, userService, jwtUtils);
    }

    @Test
    void itShouldCreateRefreshTokenSuccessfully() {
        when(userService.findById(user.getId())).thenReturn(user);
        when(refreshTokenRepository.findByUser(user)).thenReturn(Optional.empty());
        ReflectionTestUtils.setField(refreshTokenService, "refreshTokenDuration", 1800000L);
        when(refreshTokenRepository.save(any(RefreshToken.class))).thenReturn(refreshToken);
        RefreshTokenResponse refreshTokenResponse = refreshTokenService.create(user.getId());
        assertEquals(refreshTokenResponse.getToken(), refreshToken.getToken());
    }

    @Test
    void itShouldDeleteAndCreateNewRefreshToken() {
        when(userService.findById(user.getId())).thenReturn(user);
        when(refreshTokenRepository.findByUser(user)).thenReturn(Optional.of(refreshToken));
        ReflectionTestUtils.setField(refreshTokenService, "refreshTokenDuration", 1800000L);
        when(refreshTokenRepository.save(any(RefreshToken.class))).thenReturn(refreshToken);
        RefreshTokenResponse refreshTokenResponse = refreshTokenService.create(user.getId());
        assertEquals(refreshTokenResponse.getToken(), refreshToken.getToken());
    }

    @Test
    void itShouldVerifyRefreshTokenThenReturnSignInResponse() {
        when(refreshTokenRepository.findByToken(refreshToken.getToken())).thenReturn(Optional.of(refreshToken));
        refreshTokenService.verifyRefreshToken(refreshToken.getToken());
        verify(refreshTokenRepository, times(1)).findByToken(refreshToken.getToken());
    }

    @Test
    void itShouldThrowExpiredTokenExceptionWhenVerifyRefreshToken() {
        Optional<RefreshToken> token = Optional.of(new RefreshToken(user, UUID.randomUUID().toString(), System.currentTimeMillis() - 1000));
        when(refreshTokenRepository.findByToken(refreshToken.getToken())).thenReturn(token);
        assertThrows(ExpiredTokenException.class, () -> refreshTokenService.verifyRefreshToken(refreshToken.getToken()));
        verify(refreshTokenRepository, times(1)).delete(token.get());
    }

    @Test
    void itShouldDeleteRefreshTokenSuccessfully() {
        when(userService.findById("1")).thenReturn(user);
        refreshTokenService.delete(user.getId());
        verify(refreshTokenRepository, times(1)).deleteByUser(user);
    }
}