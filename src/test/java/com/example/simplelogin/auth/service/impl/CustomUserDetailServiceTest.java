package com.example.simplelogin.auth.service.impl;

import com.example.simplelogin.auth.entity.User;
import com.example.simplelogin.auth.entity.UserDetailImpl;
import com.example.simplelogin.auth.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class CustomUserDetailServiceTest {

    @Mock
    private UserRepository userRepository;

    private CustomUserDetailService customUserDetailService;

    @BeforeEach
    void setUp() {
        customUserDetailService = new CustomUserDetailService(userRepository);
    }

    @Test
    void itShouldReturnUserDetailWhenLoadUserByUsername() {
        User user = new User("1", "test@mail.com", "password");
        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.of(user));
        UserDetails userDetails = customUserDetailService.loadUserByUsername(user.getEmail());
        verify(userRepository, times(1)).findByEmail(any(String.class));
        assertEquals(user.getEmail(), userDetails.getUsername());
    }
}