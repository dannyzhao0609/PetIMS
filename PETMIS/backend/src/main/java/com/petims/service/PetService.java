package com.petims.service;

import com.petims.model.Pet;
import com.petims.repository.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PetService {
    @Autowired
    private PetRepository petRepository;
    
    public Pet addPet(Pet pet) {
        return petRepository.save(pet);
    }
    
    public Pet updatePet(Pet pet) {
        return petRepository.save(pet);
    }
    
    public void deletePet(Long id) {
        petRepository.deleteById(id);
    }
    
    public Optional<Pet> getPetById(Long id) {
        return petRepository.findById(id);
    }
    
    public List<Pet> getPetsByUserId(Long userId) {
        return petRepository.findByUserId(userId);
    }
    
    public List<Pet> getPetsByTenantId(Long tenantId) {
        return petRepository.findByTenantId(tenantId);
    }
    
    public List<Pet> getPetsByUserIdAndTenantId(Long userId, Long tenantId) {
        return petRepository.findByUserIdAndTenantId(userId, tenantId);
    }
}