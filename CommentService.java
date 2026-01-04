package com.example.demo3.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo3.Repo.CommentRepo;
import com.example.demo3.models.Comment;

@Service
public class CommentService {

    private CommentRepo commentRepo;

    public CommentService(CommentRepo commentRepo) {

        this.commentRepo = commentRepo;
    }

    // Save a comment
    public void addComment(Comment comment) {

        if (comment == null) {
            throw new RuntimeException("Required fields missing");

        }

        LocalDate createdDate = LocalDate.now();

        comment.setCreatedAt(createdDate);

        commentRepo.save(comment);

    }

    // Get all comments
    public List<Comment> getPostComments(Long postId) {

        List<Comment> comments = commentRepo.findAllByPostId(postId);

        List<Comment> sortedComments = comments.reversed();

        return sortedComments;

    }

    // Delete a coment
    public void deleteComment(Long id) {

        if (!commentRepo.existsById(id)) {
            throw new RuntimeException("No id matching " + id + " was found");
        }

        commentRepo.deleteById(id);

    }
}
