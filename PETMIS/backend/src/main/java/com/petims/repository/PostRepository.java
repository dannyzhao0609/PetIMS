package com.petims.repository;

import com.petims.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByUserId(Long userId);
    List<Post> findByPetId(Long petId);
    List<Post> findByTenantId(Long tenantId);
    List<Post> findByOrderByCreateTimeDesc();
}