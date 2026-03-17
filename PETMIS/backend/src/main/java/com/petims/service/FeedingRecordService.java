package com.petims.service;

import com.petims.model.FeedingRecord;
import com.petims.repository.FeedingRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Date;

@Service
public class FeedingRecordService {
    @Autowired
    private FeedingRecordRepository feedingRecordRepository;
    
    public FeedingRecord addFeedingRecord(FeedingRecord feedingRecord) {
        return feedingRecordRepository.save(feedingRecord);
    }
    
    public void deleteFeedingRecord(Long id) {
        feedingRecordRepository.deleteById(id);
    }
    
    public Optional<FeedingRecord> getFeedingRecordById(Long id) {
        return feedingRecordRepository.findById(id);
    }
    
    public List<FeedingRecord> getFeedingRecordsByPetId(Long petId) {
        return feedingRecordRepository.findByPetId(petId);
    }
    
    public List<FeedingRecord> getFeedingRecordsByPetIdAndDateRange(Long petId, Date startDate, Date endDate) {
        return feedingRecordRepository.findByPetIdAndFeedingTimeBetween(petId, startDate, endDate);
    }
    
    public List<FeedingRecord> getFeedingRecordsByTenantId(Long tenantId) {
        return feedingRecordRepository.findByTenantId(tenantId);
    }
}