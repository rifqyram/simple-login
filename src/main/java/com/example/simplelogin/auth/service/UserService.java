package com.example.simplelogin.auth.service;

import com.example.simplelogin.auth.entity.User;
import com.example.simplelogin.auth.model.UserResponse;
import org.springframework.security.core.Authentication;

public interface UserService {

    UserResponse get(String id);
    User findById(String id);
    UserResponse getWithAuth(Authentication authentication);

}
