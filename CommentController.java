package com.example.demo3.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo3.models.Comment;
import com.example.demo3.service.CommentService;

@RestController
public class CommentController {

    private CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    // Save Comment
    @PostMapping("/post/addComment")
    public ResponseEntity<?> addComment(@RequestBody Comment comment) {

        Map<String, String> controllerResponse = new HashMap<>();

        try {
            commentService.addComment(comment);
            controllerResponse.put("message", "Comment added successfully!");

            return ResponseEntity.status(201).body(controllerResponse);

        } catch (Exception e) {
            controllerResponse.put("message", e.getMessage());
            return ResponseEntity.status(400).body(controllerResponse);
        }
    }

    // Get All Comments on post
    @GetMapping("posts/{postId}/comments")
    public ResponseEntity<?> getPostComments(@PathVariable Long postId) {

        List<Comment> comments = commentService.getPostComments(postId);

        System.out.println(comments);

        return ResponseEntity.ok(comments);
    }

    // Delete a comment
    @DeleteMapping("/post/comment/{id}")
    public ResponseEntity<?> deleteComment(@PathVariable Long id) {

        Map<String, String> controllerResponse = new HashMap<>();

        try {
            commentService.deleteComment(id);

            controllerResponse.put("message", "Comment deleted successfully!");
            return ResponseEntity.ok(controllerResponse);

        } catch (Exception e) {

            controllerResponse.put("message", e.getMessage());

            return ResponseEntity.status(404).body(controllerResponse);
        }

    }

}
