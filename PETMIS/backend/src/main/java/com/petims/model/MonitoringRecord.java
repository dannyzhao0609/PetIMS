package com.petims.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Date;

@Data
@Entity
@Table(name = "monitoring_records")
public class MonitoringRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "pet_id", nullable = false)
    private Long petId;
    
    @Column(name = "record_type", nullable = false, length = 50)
    private String recordType;
    
    @Column(name = "value", length = 100)
    private String value;
    
    @Column(name = "unit", length = 20)
    private String unit;
    
    @Column(name = "record_time", nullable = false)
    private Date recordTime;
    
    @Column(name = "tenant_id", nullable = false)
    private Long tenantId;
    
    @Column(name = "create_time", nullable = false)
    private Date createTime;
    
    @PrePersist
    protected void onCreate() {
        createTime = new Date();
    }
}