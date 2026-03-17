package com.petims.service;

import com.petims.model.Post;
import com.petims.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostService {
    @Autowired
    private PostRepository postRepository;
    
    public Post addPost(Post post) {
        return postRepository.save(post);
    }
    
    public Post updatePost(Post post) {
        return postRepository.save(post);
    }
    
    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }
    
    public Optional<Post> getPostById(Long id) {
        return postRepository.findById(id);
    }
    
    public List<Post> getPostsByUserId(Long userId) {
        return postRepository.findByUserId(userId);
    }
    
    public List<Post> getPostsByPetId(Long petId) {
        return postRepository.findByPetId(petId);
    }
    
    public List<Post> getPostsByTenantId(Long tenantId) {
        return postRepository.findByTenantId(tenantId);
    }
    
    public List<Post> getAllPostsOrderByCreateTimeDesc() {
        return postRepository.findByOrderByCreateTimeDesc();
    }
    
    public Post incrementLikes(Long id) {
        Optional<Post> optionalPost = postRepository.findById(id);
        if (optionalPost.isPresent()) {
            Post post = optionalPost.get();
            post.setLikes(post.getLikes() + 1);
            return postRepository.save(post);
        }
        return null;
    }
    
    public Post incrementComments(Long id) {
        Optional<Post> optionalPost = postRepository.findById(id);
        if (optionalPost.isPresent()) {
            Post post = optionalPost.get();
            post.setComments(post.getComments() + 1);
            return postRepository.save(post);
        }
        return null;
    }
}