package com.petims.controller;

import com.petims.model.FeedingRecord;
import com.petims.service.FeedingRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Date;

@RestController
@RequestMapping("/api/feeding-records")
public class FeedingRecordController {
    @Autowired
    private FeedingRecordService feedingRecordService;
    
    @GetMapping
    public ResponseEntity<List<FeedingRecord>> getFeedingRecords(@RequestParam(required = false) Long petId, @RequestParam(required = false) Long startDate, @RequestParam(required = false) Long endDate) {
        List<FeedingRecord> feedingRecords;
        if (petId != null && startDate != null && endDate != null) {
            feedingRecords = feedingRecordService.getFeedingRecordsByPetIdAndDateRange(petId, new Date(startDate), new Date(endDate));
        } else if (petId != null) {
            feedingRecords = feedingRecordService.getFeedingRecordsByPetId(petId);
        } else {
            // 这里可以根据实际情况返回当前用户的宠物饮食记录列表
            // 暂时返回空列表
            feedingRecords = List.of();
        }
        return ResponseEntity.ok(feedingRecords);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<FeedingRecord> getFeedingRecordById(@PathVariable Long id) {
        Optional<FeedingRecord> feedingRecord = feedingRecordService.getFeedingRecordById(id);
        return feedingRecord.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<FeedingRecord> addFeedingRecord(@RequestBody FeedingRecord feedingRecord) {
        FeedingRecord addedFeedingRecord = feedingRecordService.addFeedingRecord(feedingRecord);
        return ResponseEntity.ok(addedFeedingRecord);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFeedingRecord(@PathVariable Long id) {
        feedingRecordService.deleteFeedingRecord(id);
        return ResponseEntity.noContent().build();
    }
}