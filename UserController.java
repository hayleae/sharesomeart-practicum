package com.example.demo3.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.example.demo3.service.FollowerService;
import com.example.demo3.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import com.example.demo3.dto.LoginRequest;
import com.example.demo3.dto.SearchTermRequest;
import com.example.demo3.models.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
public class UserController {

    private UserService userService;
    private FollowerService followerService;

    public UserController(UserService userService, FollowerService followerService) {
        this.userService = userService;
        this.followerService = followerService;
    }

    @GetMapping("/currentUser")
    public ResponseEntity<?> getCurrentUser(HttpServletRequest request) {

        // controller response
        Map<String, String> controllerResponse = new HashMap<>();

        try {

            HttpSession session = request.getSession();
            User user = (User) session.getAttribute("user");

            controllerResponse.put("id", Long.toString(user.getId()));
            controllerResponse.put("username", user.getUsername());
            controllerResponse.put("email", user.getEmail());
            controllerResponse.put("followers", Long.toString(followerService.getNumFollowedUsers(user.getId())));
            controllerResponse.put("following", Long.toString(followerService.getNumFollowers(user.getId())));

            return ResponseEntity.ok(controllerResponse);
        } catch (Exception e) {
            controllerResponse.put("message", e.getMessage());

            return ResponseEntity.status(404).body(controllerResponse);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> registUser(@RequestBody User user) {

        // controller response
        Map<String, String> controllerResponse = new HashMap<>();

        try {
            userService.registerUser(user);

            controllerResponse.put("message", "User successfully registered");

            return ResponseEntity.ok(controllerResponse);

        } catch (Exception e) {

            controllerResponse.put("message", e.getMessage());

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(controllerResponse);
        }

    }

    // User login
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest, HttpServletRequest request) {

        // controller response
        Map<String, String> controllerResponse = new HashMap<>();

        try {
            User user = userService.loginUser(loginRequest);

            HttpSession session = request.getSession();

            session.setAttribute("user", user);

            controllerResponse.put("message", "Successful Login");

            return ResponseEntity.ok(controllerResponse);

        } catch (Exception e) {

            controllerResponse.put("message", e.getMessage());

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(controllerResponse);
        }

    }

    @GetMapping("/User/{id}")
    public ResponseEntity<?> getUser(@PathVariable Long id) {

        // controller response
        Map<String, String> controllerResponse = new HashMap<>();

        try {
            User user = userService.getUser(id).get();

            controllerResponse.put("id", Long.toString(user.getId()));
            controllerResponse.put("username", user.getUsername());
            controllerResponse.put("email", user.getEmail());
            controllerResponse.put("followers", Long.toString(followerService.getNumFollowedUsers(user.getId())));
            controllerResponse.put("following", Long.toString(followerService.getNumFollowers(user.getId())));

            return ResponseEntity.ok(controllerResponse);
        } catch (Exception e) {
            controllerResponse.put("message", e.getMessage());

            return ResponseEntity.status(404).body(controllerResponse);
        }

    }

    @PostMapping("/logout")
    public ResponseEntity<?> logOut(HttpSession session) {

        // controller response
        Map<String, String> controllerResponse = new HashMap<>();

        session.invalidate();

        controllerResponse.put("message", "User logged out successfully!");

        return ResponseEntity.ok(controllerResponse);
    }

    @PostMapping("/Search/user/")
    public ResponseEntity<?> searchUser(@RequestBody SearchTermRequest searchTermRequest) {
        // controller response
        Map<String, String> controllerResponse = new HashMap<>();
        try {

            String keyword = searchTermRequest.getKeyword();

            List<User> users = userService.findAllUsernamesContainingKeyword(keyword);

            return ResponseEntity.ok(users);

        } catch (Exception e) {

            controllerResponse.put("message", e.getMessage());

            return ResponseEntity.status(404).body(controllerResponse);
        }

    }

}
