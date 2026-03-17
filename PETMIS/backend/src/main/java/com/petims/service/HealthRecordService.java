package com.petims.service;

import com.petims.model.HealthRecord;
import com.petims.repository.HealthRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HealthRecordService {
    @Autowired
    private HealthRecordRepository healthRecordRepository;
    
    public HealthRecord addHealthRecord(HealthRecord healthRecord) {
        return healthRecordRepository.save(healthRecord);
    }
    
    public HealthRecord updateHealthRecord(HealthRecord healthRecord) {
        return healthRecordRepository.save(healthRecord);
    }
    
    public void deleteHealthRecord(Long id) {
        healthRecordRepository.deleteById(id);
    }
    
    public Optional<HealthRecord> getHealthRecordById(Long id) {
        return healthRecordRepository.findById(id);
    }
    
    public List<HealthRecord> getHealthRecordsByPetId(Long petId) {
        return healthRecordRepository.findByPetId(petId);
    }
    
    public List<HealthRecord> getHealthRecordsByPetIdAndRecordType(Long petId, String recordType) {
        return healthRecordRepository.findByPetIdAndRecordType(petId, recordType);
    }
    
    public List<HealthRecord> getHealthRecordsByTenantId(Long tenantId) {
        return healthRecordRepository.findByTenantId(tenantId);
    }
}