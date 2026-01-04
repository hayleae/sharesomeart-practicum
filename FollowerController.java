package com.example.demo3.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo3.models.Follower;
import com.example.demo3.models.User;
import com.example.demo3.service.FollowerService;
import com.example.demo3.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@RestController
public class FollowerController {
        
    private FollowerService followerService;
    private UserService userService;

    public FollowerController (FollowerService followerService, UserService userService){
        this.followerService = followerService;
        this.userService = userService;
    }

    @GetMapping("/followers/count")
    public int getFollowerCount(@RequestParam Long userId) {
        return followerService.getNumFollowers(userId);
    }

    @GetMapping("/followed/count")
    public int getFollowedCount(@RequestParam Long userId) {
        return followerService.getNumFollowedUsers(userId);
    }

    @GetMapping("/followed/all")
    public List<Follower> getAllFollowedAccounts(@RequestParam Long userId) {
        return followerService.getAllFollowedUsers(userId);
    }

    @PostMapping("/follow")
    public ResponseEntity<?> follow(@RequestBody Map<String, String> body, HttpServletRequest request) {

        // controller response
        Map<String, String> controllerResponse = new HashMap<>();

        try {

            HttpSession session = request.getSession();
            User currentUser = (User) session.getAttribute("user");
            Long followedId = Long.parseLong(body.get("followedId"));
            System.out.println(currentUser.getId() +  " " + followedId);
            followerService.followUser(currentUser.getId(), followedId);
            controllerResponse.put("message", "Succesfully followed!");
            return ResponseEntity.ok(controllerResponse);

        } catch (Exception e) {
            controllerResponse.put("message", e.getMessage());

            return ResponseEntity.status(400).body(controllerResponse);
        }
    }

    @PostMapping("/unfollow")
    public ResponseEntity<?> unfollow(@RequestBody Map<String, String> body, HttpServletRequest request) {
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("user");
        Long followedId = Long.parseLong(body.get("followedId"));
        followerService.unfollowUser(currentUser.getId(), followedId);
        return ResponseEntity.ok().build();
    }

    
    @GetMapping("/api/is-following/{username}")
    public Map<String, Boolean> isFollowing(@PathVariable String username,
            HttpServletRequest request) {
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("user");

        User targetUser = userService.findByUsername(username);
        boolean following = followerService.isFollowed(currentUser.getId(),
                targetUser.getId());

        return Map.of("isFollowing", following);
    }
    
}
