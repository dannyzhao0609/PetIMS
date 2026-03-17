package com.petims.repository;

import com.petims.model.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {
    List<Pet> findByUserId(Long userId);
    List<Pet> findByTenantId(Long tenantId);
    List<Pet> findByUserIdAndTenantId(Long userId, Long tenantId);
}