package com.petims.controller;

import com.petims.model.Pet;
import com.petims.service.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/pets")
public class PetController {
    @Autowired
    private PetService petService;
    
    @GetMapping
    public ResponseEntity<List<Pet>> getPets(@RequestParam(required = false) Long userId) {
        List<Pet> pets;
        if (userId != null) {
            pets = petService.getPetsByUserId(userId);
        } else {
            // 这里可以根据实际情况返回当前用户的宠物列表
            // 暂时返回空列表
            pets = List.of();
        }
        return ResponseEntity.ok(pets);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Pet> getPetById(@PathVariable Long id) {
        Optional<Pet> pet = petService.getPetById(id);
        return pet.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<Pet> addPet(@RequestBody Pet pet) {
        Pet addedPet = petService.addPet(pet);
        return ResponseEntity.ok(addedPet);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Pet> updatePet(@PathVariable Long id, @RequestBody Pet pet) {
        pet.setId(id);
        Pet updatedPet = petService.updatePet(pet);
        return ResponseEntity.ok(updatedPet);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePet(@PathVariable Long id) {
        petService.deletePet(id);
        return ResponseEntity.noContent().build();
    }
}