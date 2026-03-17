package com.petims.controller;

import com.petims.model.HealthRecord;
import com.petims.service.HealthRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/health-records")
public class HealthRecordController {
    @Autowired
    private HealthRecordService healthRecordService;
    
    @GetMapping
    public ResponseEntity<List<HealthRecord>> getHealthRecords(@RequestParam(required = false) Long petId, @RequestParam(required = false) String recordType) {
        List<HealthRecord> healthRecords;
        if (petId != null && recordType != null) {
            healthRecords = healthRecordService.getHealthRecordsByPetIdAndRecordType(petId, recordType);
        } else if (petId != null) {
            healthRecords = healthRecordService.getHealthRecordsByPetId(petId);
        } else {
            // 这里可以根据实际情况返回当前用户的宠物健康记录列表
            // 暂时返回空列表
            healthRecords = List.of();
        }
        return ResponseEntity.ok(healthRecords);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<HealthRecord> getHealthRecordById(@PathVariable Long id) {
        Optional<HealthRecord> healthRecord = healthRecordService.getHealthRecordById(id);
        return healthRecord.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<HealthRecord> addHealthRecord(@RequestBody HealthRecord healthRecord) {
        HealthRecord addedHealthRecord = healthRecordService.addHealthRecord(healthRecord);
        return ResponseEntity.ok(addedHealthRecord);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<HealthRecord> updateHealthRecord(@PathVariable Long id, @RequestBody HealthRecord healthRecord) {
        healthRecord.setId(id);
        HealthRecord updatedHealthRecord = healthRecordService.updateHealthRecord(healthRecord);
        return ResponseEntity.ok(updatedHealthRecord);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHealthRecord(@PathVariable Long id) {
        healthRecordService.deleteHealthRecord(id);
        return ResponseEntity.noContent().build();
    }
}