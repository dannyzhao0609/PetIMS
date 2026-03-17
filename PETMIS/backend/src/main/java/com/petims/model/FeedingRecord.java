package com.petims.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Date;

@Data
@Entity
@Table(name = "feeding_records")
public class FeedingRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "pet_id", nullable = false)
    private Long petId;
    
    @Column(name = "food_name", nullable = false, length = 100)
    private String foodName;
    
    @Column(name = "amount", precision = 5, scale = 2)
    private Double amount;
    
    @Column(name = "unit", length = 20)
    private String unit;
    
    @Column(name = "feeding_time", nullable = false)
    private Date feedingTime;
    
    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;
    
    @Column(name = "tenant_id", nullable = false)
    private Long tenantId;
    
    @Column(name = "create_time", nullable = false)
    private Date createTime;
    
    @PrePersist
    protected void onCreate() {
        createTime = new Date();
    }
}