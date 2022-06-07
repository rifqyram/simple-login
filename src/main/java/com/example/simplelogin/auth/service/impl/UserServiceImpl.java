package com.example.simplelogin.auth.service.impl;

import com.example.simplelogin.auth.entity.User;
import com.example.simplelogin.auth.entity.UserDetailImpl;
import com.example.simplelogin.auth.model.UserResponse;
import com.example.simplelogin.auth.repository.UserRepository;
import com.example.simplelogin.auth.service.UserService;
import com.example.simplelogin.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserResponse get(String id) {
        return findByIdOrThrowNotFound(id).toUserResponse();
    }

    @Override
    public User findById(String id) {
        return findByIdOrThrowNotFound(id);
    }

    @Override
    public UserResponse getWithAuth(Authentication authentication) {
        UserDetailImpl userDetails = (UserDetailImpl) authentication.getPrincipal();
        return findByIdOrThrowNotFound(userDetails.getId()).toUserResponse();
    }

    private User findByIdOrThrowNotFound(String id) {
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found"));
    }

}
