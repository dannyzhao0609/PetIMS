package com.petims.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Date;

@Data
@Entity
@Table(name = "hygiene_records")
public class HygieneRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "pet_id", nullable = false)
    private Long petId;
    
    @Column(name = "record_type", nullable = false, length = 50)
    private String recordType;
    
    @Column(name = "date", nullable = false)
    private Date date;
    
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "attachments", length = 255)
    private String attachments;
    
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