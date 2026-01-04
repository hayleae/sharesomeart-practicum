package com.example.demo3.Repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo3.models.Comment;

@Repository
public interface CommentRepo extends JpaRepository<Comment, Long> {

    public List<Comment> findAllByPostId(Long postId);

}