package com.petims.repository;

import com.petims.model.FeedingRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Date;

@Repository
public interface FeedingRecordRepository extends JpaRepository<FeedingRecord, Long> {
    List<FeedingRecord> findByPetId(Long petId);
    List<FeedingRecord> findByPetIdAndFeedingTimeBetween(Long petId, Date startDate, Date endDate);
    List<FeedingRecord> findByTenantId(Long tenantId);
}