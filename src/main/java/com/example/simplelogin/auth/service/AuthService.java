package com.example.simplelogin.auth.service;

import com.example.simplelogin.auth.model.SignInResponse;
import com.example.simplelogin.auth.model.UserRequest;
import com.example.simplelogin.auth.model.UserResponse;
import org.springframework.security.core.Authentication;

public interface AuthService {

    UserResponse signUp(UserRequest request);

    SignInResponse signIn(UserRequest request);

    String logout(Authentication authentication);

}
