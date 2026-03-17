package com.petims.controller;

import com.petims.model.HygieneRecord;
import com.petims.service.HygieneRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/hygiene-records")
public class HygieneRecordController {
    @Autowired
    private HygieneRecordService hygieneRecordService;
    
    @GetMapping
    public ResponseEntity<List<HygieneRecord>> getHygieneRecords(@RequestParam(required = false) Long petId, @RequestParam(required = false) String recordType) {
        List<HygieneRecord> hygieneRecords;
        if (petId != null && recordType != null) {
            hygieneRecords = hygieneRecordService.getHygieneRecordsByPetIdAndRecordType(petId, recordType);
        } else if (petId != null) {
            hygieneRecords = hygieneRecordService.getHygieneRecordsByPetId(petId);
        } else {
            // 这里可以根据实际情况返回当前用户的宠物卫生记录列表
            // 暂时返回空列表
            hygieneRecords = List.of();
        }
        return ResponseEntity.ok(hygieneRecords);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<HygieneRecord> getHygieneRecordById(@PathVariable Long id) {
        Optional<HygieneRecord> hygieneRecord = hygieneRecordService.getHygieneRecordById(id);
        return hygieneRecord.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<HygieneRecord> addHygieneRecord(@RequestBody HygieneRecord hygieneRecord) {
        HygieneRecord addedHygieneRecord = hygieneRecordService.addHygieneRecord(hygieneRecord);
        return ResponseEntity.ok(addedHygieneRecord);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<HygieneRecord> updateHygieneRecord(@PathVariable Long id, @RequestBody HygieneRecord hygieneRecord) {
        hygieneRecord.setId(id);
        HygieneRecord updatedHygieneRecord = hygieneRecordService.updateHygieneRecord(hygieneRecord);
        return ResponseEntity.ok(updatedHygieneRecord);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHygieneRecord(@PathVariable Long id) {
        hygieneRecordService.deleteHygieneRecord(id);
        return ResponseEntity.noContent().build();
    }
}