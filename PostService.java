package com.example.demo3.service;

import org.springframework.stereotype.Service;

import com.example.demo3.Repo.PostRepo;
import com.example.demo3.models.Post;

import java.util.*;

import javax.management.RuntimeErrorException;

@Service
public class PostService {

    private PostRepo postRepo;

    public PostService(PostRepo postRepo) {
        this.postRepo = postRepo;
    }

    public List<Post> getAllPosts() {
        return postRepo.findAll().reversed();
    }

    @SuppressWarnings(value = { "7088" })
    public Post createPost(Post post) {
        return postRepo.save(post);
    }

    public Post savePost(Post post){
        return postRepo.save(post);
    }

    public Post getById(Long id) {
        return postRepo.getPostById(id);
    }

    public List<Post> findAllByTitleContains(String keyword) {

        if (keyword.isBlank()) {
            throw new RuntimeException("No post matching, '" + keyword + "' was found");
        }

        List<Post> result = postRepo.findAllByTitleContains(keyword);

        if (result.isEmpty()) {
            throw new RuntimeException("No posts found matching provided title");
        }

        result = removeHidden(result);
        return result.reversed();
    }

    public List<Post> getAllUserPosts(Long id) {
        List<Post> list = postRepo.getAllPostByUserId(id).reversed();
        return removeHidden(list);
    }

    public List<Post> findAllByTagContains(String keyword) {
        if (keyword.isBlank()) {
            throw new RuntimeException("No post matching, '" + keyword + "' was found");
        }

        List<Post> result = postRepo.findAllByTagContains(keyword);

        if (result.isEmpty()) {
            throw new RuntimeException("No posts found matching provided tag");
        }

        result = removeHidden(result);
        return result.reversed();
    }

    public List<Post> searchByTag(String tag) {
        return postRepo.findByTagContainingIgnoreCase(tag).reversed();
    }

    public List<Post> getAllPostsFromFollowed(List<Long> idList){
        List<Post> list =  postRepo.findAllByUserIdIn(idList).reversed();
        return removeHidden(list);
    }

    //Method that removes hidden posts
    private List<Post> removeHidden(List<Post> list){

        List<Post> nonHidden = new ArrayList<Post>();

        for (int i = 0; i < list.size(); i++){
            Post post = list.get(i);
            if (post.getHidden() == false){
                nonHidden.add(post);
            }
        }

        return nonHidden;
    }

    public void SavePost(Post post) {
        postRepo.save(post);
    }

}