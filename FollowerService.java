package com.example.demo3.service;
 
import java.util.ArrayList;
import java.util.List;
 
import org.springframework.stereotype.Service;
 
import com.example.demo3.Repo.FollowerRepo;
import com.example.demo3.Repo.UserRepo;
import com.example.demo3.models.Follower;
import com.example.demo3.models.User;

import jakarta.transaction.Transactional;
 
@Service
public class FollowerService {
    private FollowerRepo repo;
    private UserRepo userRepo;
 
    public FollowerService(FollowerRepo repo, UserRepo userRepo) {
        this.repo = repo;
        this.userRepo = userRepo;
    }
 
    public int getNumFollowers(Long id) {
 
        return repo.findAllByUserId(id).size();
    }
 
    public List<Follower> getUsersFollowers(Long id) {
 
        return repo.findAllByFollowedId(id);
 
    }

    public List<Follower> getAllFollowedUsers(Long id){
        return repo.findAllByUserId(id);
    }
 
    public int getNumFollowedUsers(Long id) {
        return repo.findAllByFollowedId(id).size();
 
    }
 
    public boolean isFollowed(Long userId, Long followedId) {
        List<Follower> followers = repo.findAllByUserId(userId);
 
        Follower follower = repo.findByUserIdAndFollowedId(userId, followedId);
 
        if (followers.contains(follower)) {
            return true;
        }
        return false;
    }
 
    public void followUser(Long userId, Long followedId) {
 
        if (isFollowed(userId, followedId)) {
            throw new RuntimeException("Already following user");
        }
 
        Follower follower = new Follower();
        follower.setUserId(userId);
        follower.setFollowedId(followedId);
        repo.save(follower);
    }
 
    // Remove a follow relationship
    @Transactional
    public void unfollowUser(Long userId, Long followedId) {
        repo.deleteByUserIdAndFollowedId(userId, followedId);
    }
}
