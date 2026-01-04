package com.example.demo3.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo3.models.Follower;
import java.util.List;


/*
 * Matt Issue #28
 */
@Repository
public interface FollowerRepo extends JpaRepository<Follower, Long>{
    
    public List<Follower> findAllByUserId(Long userId);

    public List<Follower> findAllByFollowedId(Long followedId);

    public Follower findByUserIdAndFollowedId(Long userId, Long followedId);

    public void deleteByUserIdAndFollowedId(Long userId, Long followedId);
}
