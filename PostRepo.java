package com.example.demo3.Repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.demo3.models.*;

@Repository
public interface PostRepo extends JpaRepository<Post, Long> {
    List<Post> findByTagContainingIgnoreCase(String tag);

    public Post getPostById(Long id);

    public List<Post> findAllByTagContains(String tag);

    public List<Post> getAllPostByUserId(Long userId);

    public List<Post> findAllByTitleContains(String keyword);

    public List<Post> findAllByUserIdIn(List<Long> userId);

}
