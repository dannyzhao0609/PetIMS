package com.petims.controller;

import com.petims.model.MonitoringRecord;
import com.petims.service.MonitoringRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Date;

@RestController
@RequestMapping("/api/monitoring-records")
public class MonitoringRecordController {
    @Autowired
    private MonitoringRecordService monitoringRecordService;
    
    @GetMapping
    public ResponseEntity<List<MonitoringRecord>> getMonitoringRecords(@RequestParam(required = false) Long petId, @RequestParam(required = false) String recordType, @RequestParam(required = false) Long startDate, @RequestParam(required = false) Long endDate) {
        List<MonitoringRecord> monitoringRecords;
        if (petId != null && recordType != null && startDate != null && endDate != null) {
            monitoringRecords = monitoringRecordService.getMonitoringRecordsByPetIdAndRecordTypeAndDateRange(petId, recordType, new Date(startDate), new Date(endDate));
        } else if (petId != null && recordType != null) {
            monitoringRecords = monitoringRecordService.getMonitoringRecordsByPetIdAndRecordType(petId, recordType);
        } else if (petId != null) {
            monitoringRecords = monitoringRecordService.getMonitoringRecordsByPetId(petId);
        } else {
            // 这里可以根据实际情况返回当前用户的宠物监控记录列表
            // 暂时返回空列表
            monitoringRecords = List.of();
        }
        return ResponseEntity.ok(monitoringRecords);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<MonitoringRecord> getMonitoringRecordById(@PathVariable Long id) {
        Optional<MonitoringRecord> monitoringRecord = monitoringRecordService.getMonitoringRecordById(id);
        return monitoringRecord.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<MonitoringRecord> addMonitoringRecord(@RequestBody MonitoringRecord monitoringRecord) {
        MonitoringRecord addedMonitoringRecord = monitoringRecordService.addMonitoringRecord(monitoringRecord);
        return ResponseEntity.ok(addedMonitoringRecord);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMonitoringRecord(@PathVariable Long id) {
        monitoringRecordService.deleteMonitoringRecord(id);
        return ResponseEntity.noContent().build();
    }
}