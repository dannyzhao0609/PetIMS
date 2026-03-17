package com.petims.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Date;

@Data
@Entity
@Table(name = "pets")
public class Pet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "name", nullable = false, length = 50)
    private String name;
    
    @Column(name = "species", nullable = false, length = 50)
    private String species;
    
    @Column(name = "breed", length = 50)
    private String breed;
    
    @Column(name = "gender", length = 10)
    private String gender;
    
    @Column(name = "birthday")
    private Date birthday;
    
    @Column(name = "weight", precision = 5, scale = 2)
    private Double weight;
    
    @Column(name = "height", precision = 5, scale = 2)
    private Double height;
    
    @Column(name = "user_id", nullable = false)
    private Long userId;
    
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
    }
    
    @PreUpdate
    protected void onUpdate() {
        updateTime = new Date();
    }
}