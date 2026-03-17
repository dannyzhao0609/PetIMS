package com.petims.repository;

import com.petims.model.HealthRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HealthRecordRepository extends JpaRepository<HealthRecord, Long> {
    List<HealthRecord> findByPetId(Long petId);
    List<HealthRecord> findByPetIdAndRecordType(Long petId, String recordType);
    List<HealthRecord> findByTenantId(Long tenantId);
}