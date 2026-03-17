package com.petims.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Date;

@Data
@Entity
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "user_id", nullable = false)
    private Long userId;
    
    @Column(name = "pet_id")
    private Long petId;
    
    @Column(name = "title", nullable = false, length = 100)
    private String title;
    
    @Column(name = "content", columnDefinition = "TEXT")
    private String content;
    
    @Column(name = "attachments", length = 255)
    private String attachments;
    
    @Column(name = "likes")
    private Integer likes;
    
    @Column(name = "comments")
    private Integer comments;
    
    @Column(name = "tenant_id", nullable = false)
    private Long tenantId;
    
    @Column(name = "create_time", nullable = false)
    private Date createTime;
    
    @Column(name = "update_time", nullable = false)
    private Date updateTime;
    
    @PrePersist
    protected void onCreate() {
        createTime = new Date();
        updateTime = new Date();
        likes = 0;
        comments = 0;
    }
    
    @PreUpdate
    protected void onUpdate() {
        updateTime = new Date();
    }
}