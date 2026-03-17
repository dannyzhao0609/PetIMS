package com.petims.service;

import com.petims.model.HygieneRecord;
import com.petims.repository.HygieneRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HygieneRecordService {
    @Autowired
    private HygieneRecordRepository hygieneRecordRepository;
    
    public HygieneRecord addHygieneRecord(HygieneRecord hygieneRecord) {
        return hygieneRecordRepository.save(hygieneRecord);
    }
    
    public HygieneRecord updateHygieneRecord(HygieneRecord hygieneRecord) {
        return hygieneRecordRepository.save(hygieneRecord);
    }
    
    public void deleteHygieneRecord(Long id) {
        hygieneRecordRepository.deleteById(id);
    }
    
    public Optional<HygieneRecord> getHygieneRecordById(Long id) {
        return hygieneRecordRepository.findById(id);
    }
    
    public List<HygieneRecord> getHygieneRecordsByPetId(Long petId) {
        return hygieneRecordRepository.findByPetId(petId);
    }
    
    public List<HygieneRecord> getHygieneRecordsByPetIdAndRecordType(Long petId, String recordType) {
        return hygieneRecordRepository.findByPetIdAndRecordType(petId, recordType);
    }
    
    public List<HygieneRecord> getHygieneRecordsByTenantId(Long tenantId) {
        return hygieneRecordRepository.findByTenantId(tenantId);
    }
}