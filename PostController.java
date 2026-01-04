package com.example.demo3.controllers;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo3.service.PostService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.IOException;
import java.util.*;
import java.util.Collections;

import com.example.demo3.dto.SearchTermRequest;
import com.example.demo3.models.Post;
import com.example.demo3.models.User;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class PostController {

    private PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    // Change to use PostId in future change
    @GetMapping("/AllPosts")
    public List<Post> getAllPosts() {
        return postService.getAllPosts();
    }

    @PostMapping("/create-post")
    public ResponseEntity<?> CreatePost(@RequestParam(name = "title") String title,
            @RequestParam(name = "description") String description,
            @RequestParam(name = "tag") String tag, @RequestParam(name = "image") MultipartFile image,
            HttpServletRequest request) throws IOException {
            
            Map<String, String> controllerResponse = new HashMap<>();

            Post post = new Post();

            post.setImage(Base64.getEncoder().encodeToString(image.getBytes()));
            post.setTitle(title);
            post.setDescription(description);
            post.setTag(tag);
            //System.out.println("Post Information: \n Title: " + title
           //+ "\nDescription: " + description + "\nTag: " + tag + "\nImage: " + post.getImage());
           
        try {
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute("user");
            post.setUserId(user.getId());
            post.setAuthor(user.getUsername());
            postService.createPost(post);

                controllerResponse.put("message", "Post successfully created");

                return ResponseEntity.ok(controllerResponse);

            } catch (Exception e) {

                controllerResponse.put("message", e.getMessage());

                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(controllerResponse);
            }

        }

        @PostMapping("/save-post")
        public ResponseEntity<?> SavePost(@RequestParam(name = "postId") Long postId,
                @RequestParam(name = "title", required = false) String title,
                @RequestParam(name = "description", required = false) String description,
                @RequestParam(name = "tag", required = false) String tag,
                @RequestParam(name = "image", required = false) MultipartFile image) throws IOException {

            System.out.println("Post id" + postId);

            Map<String, String> controllerResponse = new HashMap<>();

            try {
                Post post = postService.getById(postId);
                if (post == null) {
                    controllerResponse.put("message", "Post not found");
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(controllerResponse);
                }

                if (title != null && !title.isEmpty()) {
                    post.setTitle(title);
                }

                if (description != null && !description.isEmpty()) {
                    post.setDescription(description);
                }

                if (tag != null && !tag.isEmpty()) {
                    post.setTag(tag);
                }

                if (image != null && !image.isEmpty()) {
                    post.setImage(Base64.getEncoder().encodeToString(image.getBytes()));
                }

                postService.SavePost(post);

                controllerResponse.put("message", "Post successfully updated");
                return ResponseEntity.ok(controllerResponse);

            } catch (Exception e) {
                controllerResponse.put("message", e.getMessage());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(controllerResponse);
            }
    }

       @GetMapping("/Post/user/{id}")
       public List<Post> getAllUserPosts(@PathVariable Long id) {
           return postService.getAllUserPosts(id);
       }

    @GetMapping("/Post/postId/{id}")
    public ResponseEntity<?> getPostbyId(@PathVariable Long id) {

        // controller response
        Map<String, String> controllerResponse = new HashMap<>();

        try {

            Post post = postService.getById(id);

            return ResponseEntity.ok(post);

        } catch (Exception e) {

            controllerResponse.put("message", e.getMessage());
            return ResponseEntity.status(404).body(controllerResponse);
        }
    }

    @PostMapping("/Search/title/")
    public ResponseEntity<?> getPostByContainsKeyword(@RequestBody SearchTermRequest searchTermRequest) {

        // controller response
        Map<String, String> controllerResponse = new HashMap<>();

        try {

            String keyword = searchTermRequest.getKeyword();

            List<Post> posts = postService.findAllByTitleContains(keyword);

            return ResponseEntity.ok(posts);

        } catch (Exception e) {

            controllerResponse.put("message", e.getMessage());

            return ResponseEntity.status(404).body(controllerResponse);

        }

    }

    @PostMapping("/Search/tag/")
    public ResponseEntity<?> getPostByTagContains(@RequestBody SearchTermRequest searchTermRequest) {

        // controller response

        Map<String, String> controllerResponse = new HashMap<>();

        try {

            String keyword = searchTermRequest.getKeyword();

            List<Post> posts = postService.findAllByTagContains(keyword);

            return ResponseEntity.ok(posts);

        } catch (Exception e) {

            controllerResponse.put("message", e.getMessage());

            return ResponseEntity.status(404).body(controllerResponse);

        }
    } 

    @GetMapping("/Post/users")
    public List<Post> getAllFollowedPosts(@RequestParam List<Long> ids) {
        /*
        List<Post> posts = new ArrayList<Post>();
        for(Long id: ids){
            List<Post> userPosts = postService.getAllUserPosts(id);
            posts.addAll(userPosts);
        }
        */
        
        //Add sorting by PostId later
        return postService.getAllPostsFromFollowed(ids);
    }

    @GetMapping("/Post/delete")
    public ResponseEntity<?> deletePost(@RequestParam Long id){

        Map<String, String> controllerResponse = new HashMap<>();

        Post post = postService.getById(id);
        try {
            post.setHidden();
            postService.savePost(post);
            return ResponseEntity.ok(post);
        } catch (Exception e){
            return ResponseEntity.status(404).body(controllerResponse);
        }
    }
    
       
}
