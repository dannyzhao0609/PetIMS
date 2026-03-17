package com.petims.repository;

import com.petims.model.HygieneRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HygieneRecordRepository extends JpaRepository<HygieneRecord, Long> {
    List<HygieneRecord> findByPetId(Long petId);
    List<HygieneRecord> findByPetIdAndRecordType(Long petId, String recordType);
    List<HygieneRecord> findByTenantId(Long tenantId);
}