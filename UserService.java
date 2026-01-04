package com.example.demo3.service;

import org.springframework.stereotype.Service;
import com.example.demo3.Repo.UserRepo;
import com.example.demo3.dto.LoginRequest;
import com.example.demo3.models.User;
import java.util.*;

@Service
public class UserService {
    private UserRepo userRepo;

    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    // Find and return specific user by userId
    public Optional<User> getUser(Long id) {
        return userRepo.findById(id);
    }

    // Register user
    public User registerUser(User user) {

        if (userRepo.existsByUsername(user.getUsername())) {

            throw new RuntimeException("Username already exists!");
        }

        if (userRepo.existsByEmail(user.getEmail())) {

            throw new RuntimeException("Email already exists!");
        }

        if (user.getPassword().length() < 6) {
            throw new RuntimeException("Minimum password length is 6!");
        }
        return userRepo.save(user);
    }

    // Login user
    public User loginUser(LoginRequest loginRequest) {

        Optional<User> userOptional = userRepo.findByUsernameOrEmail(loginRequest.getUserNameOrEmail(),
                loginRequest.getUserNameOrEmail());

        if (userOptional.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        User user = userOptional.get();

        if (!user.getPassword().matches(loginRequest.getPassword())) {
            throw new RuntimeException("Wrong password. Try again!");
        }

        return user;
    }

    public List<User> findAllUsernamesContainingKeyword(String keyword) {
        if (keyword.isBlank()) {
            throw new RuntimeException("No user matching, '" + keyword + "' was found");
        }

        List<User> result = userRepo.findByUsernameContains(keyword);

        if (result.isEmpty()) {
            throw new RuntimeException("No users found");
        }

        return result;
    }

    public User findByUsername(String username) {
    return userRepo.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
