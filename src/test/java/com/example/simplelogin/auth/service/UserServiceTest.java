package com.example.simplelogin.auth.service;

import com.example.simplelogin.auth.entity.User;
import com.example.simplelogin.auth.entity.UserDetailImpl;
import com.example.simplelogin.auth.model.UserResponse;
import com.example.simplelogin.auth.repository.UserRepository;
import com.example.simplelogin.auth.service.impl.UserServiceImpl;
import com.example.simplelogin.exception.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private Authentication authentication;

    User user;

    @BeforeEach
    void setUp() {
        user = new User("1", "test@gmail.com", "password");
        userService = new UserServiceImpl(userRepository);
    }

    @Test
    void get_ItShouldReturnUserResponseById() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        UserResponse userResponse = userService.get(user.getId());
        assertEquals(user.getEmail(), userResponse.getEmail());
        verify(userRepository, times(1)).findById(user.getId());
    }

    @Test
    void itShouldThrowNotFoundExceptionWhenUserNotFound() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> userService.get(user.getId()));
    }

    @Test
    void itShouldReturnUser() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        User currentUser = userService.findById(user.getId());
        assertEquals(user, currentUser);
        verify(userRepository, times(1)).findById(user.getId());
    }

    @Test
    void itShouldReturnUserResponseWithAuth() {
        UserDetailImpl userDetail = new UserDetailImpl(user.getId(), user.getEmail(), user.getPassword());
        when(authentication.getPrincipal()).thenReturn(userDetail);
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        UserResponse userResponse = userService.getWithAuth(authentication);
        assertEquals(user.getEmail(), userResponse.getEmail());
        verify(authentication, times(1)).getPrincipal();
    }
}