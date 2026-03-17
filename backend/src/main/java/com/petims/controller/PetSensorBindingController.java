package com.petims.controller;

import com.petims.common.result.Result;
import com.petims.dto.PetSensorBindingDTO;
import com.petims.entity.PetSensorBinding;
import com.petims.service.PetSensorBindingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Tag(name = "宠物传感器绑定管理", description = "宠物传感器绑定管理接口")
@RestController
@RequestMapping("/pet-sensor-bindings")
public class PetSensorBindingController {

    private final PetSensorBindingService petSensorBindingService;

    public PetSensorBindingController(PetSensorBindingService petSensorBindingService) {
        this.petSensorBindingService = petSensorBindingService;
    }

    @Operation(summary = "获取所有绑定列表")
    @GetMapping
    public Result<List<PetSensorBindingDTO>> listAll() {
        return Result.success(petSensorBindingService.listAllDTO());
    }

    @Operation(summary = "获取宠物的绑定列表")
    @GetMapping("/pet/{petId}")
    public Result<List<PetSensorBindingDTO>> listByPetId(@PathVariable Long petId) {
        return Result.success(petSensorBindingService.listByPetIdDTO(petId));
    }

    @Operation(summary = "获取传感器的绑定列表")
    @GetMapping("/sensor/{sensorId}")
    public Result<List<PetSensorBinding>> listBySensorId(@PathVariable Long sensorId) {
        return Result.success(petSensorBindingService.listBySensorId(sensorId));
    }

    @Operation(summary = "根据ID获取绑定")
    @GetMapping("/{id}")
    public Result<PetSensorBinding> getById(@PathVariable Long id) {
        return Result.success(petSensorBindingService.getById(id));
    }

    @Operation(summary = "新增绑定")
    @PostMapping
    public Result<Void> save(@RequestBody PetSensorBinding binding) {
        binding.setBindTime(LocalDateTime.now());
        binding.setStatus("bound");
        petSensorBindingService.save(binding);
        return Result.success();
    }

    @Operation(summary = "更新绑定")
    @PutMapping
    public Result<Void> updateById(@RequestBody PetSensorBinding binding) {
        petSensorBindingService.updateById(binding);
        return Result.success();
    }

    @Operation(summary = "删除绑定")
    @DeleteMapping("/{id}")
    public Result<Void> removeById(@PathVariable Long id) {
        petSensorBindingService.removeById(id);
        return Result.success();
    }
}
