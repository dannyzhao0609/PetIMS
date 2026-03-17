package com.petims.repository;

import com.petims.model.MonitoringRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Date;

@Repository
public interface MonitoringRecordRepository extends JpaRepository<MonitoringRecord, Long> {
    List<MonitoringRecord> findByPetId(Long petId);
    List<MonitoringRecord> findByPetIdAndRecordType(Long petId, String recordType);
    List<MonitoringRecord> findByPetIdAndRecordTypeAndRecordTimeBetween(Long petId, String recordType, Date startDate, Date endDate);
    List<MonitoringRecord> findByTenantId(Long tenantId);
}