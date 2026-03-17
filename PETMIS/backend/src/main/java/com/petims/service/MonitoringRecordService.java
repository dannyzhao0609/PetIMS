package com.petims.service;

import com.petims.model.MonitoringRecord;
import com.petims.repository.MonitoringRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Date;

@Service
public class MonitoringRecordService {
    @Autowired
    private MonitoringRecordRepository monitoringRecordRepository;
    
    public MonitoringRecord addMonitoringRecord(MonitoringRecord monitoringRecord) {
        return monitoringRecordRepository.save(monitoringRecord);
    }
    
    public void deleteMonitoringRecord(Long id) {
        monitoringRecordRepository.deleteById(id);
    }
    
    public Optional<MonitoringRecord> getMonitoringRecordById(Long id) {
        return monitoringRecordRepository.findById(id);
    }
    
    public List<MonitoringRecord> getMonitoringRecordsByPetId(Long petId) {
        return monitoringRecordRepository.findByPetId(petId);
    }
    
    public List<MonitoringRecord> getMonitoringRecordsByPetIdAndRecordType(Long petId, String recordType) {
        return monitoringRecordRepository.findByPetIdAndRecordType(petId, recordType);
    }
    
    public List<MonitoringRecord> getMonitoringRecordsByPetIdAndRecordTypeAndDateRange(Long petId, String recordType, Date startDate, Date endDate) {
        return monitoringRecordRepository.findByPetIdAndRecordTypeAndRecordTimeBetween(petId, recordType, startDate, endDate);
    }
    
    public List<MonitoringRecord> getMonitoringRecordsByTenantId(Long tenantId) {
        return monitoringRecordRepository.findByTenantId(tenantId);
    }
}