package com.petims.controller;

import com.petims.model.Post;
import com.petims.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/posts")
public class PostController {
    @Autowired
    private PostService postService;
    
    @GetMapping
    public ResponseEntity<List<Post>> getPosts(@RequestParam(required = false) Long userId, @RequestParam(required = false) Long petId) {
        List<Post> posts;
        if (userId != null) {
            posts = postService.getPostsByUserId(userId);
        } else if (petId != null) {
            posts = postService.getPostsByPetId(petId);
        } else {
            posts = postService.getAllPostsOrderByCreateTimeDesc();
        }
        return ResponseEntity.ok(posts);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Post> getPostById(@PathVariable Long id) {
        Optional<Post> post = postService.getPostById(id);
        return post.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<Post> addPost(@RequestBody Post post) {
        Post addedPost = postService.addPost(post);
        return ResponseEntity.ok(addedPost);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Post> updatePost(@PathVariable Long id, @RequestBody Post post) {
        post.setId(id);
        Post updatedPost = postService.updatePost(post);
        return ResponseEntity.ok(updatedPost);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }
    
    @PostMapping("/{id}/like")
    public ResponseEntity<Post> incrementLikes(@PathVariable Long id) {
        Post post = postService.incrementLikes(id);
        return post != null ? ResponseEntity.ok(post) : ResponseEntity.notFound().build();
    }
    
    @PostMapping("/{id}/comment")
    public ResponseEntity<Post> incrementComments(@PathVariable Long id) {
        Post post = postService.incrementComments(id);
        return post != null ? ResponseEntity.ok(post) : ResponseEntity.notFound().build();
    }
}