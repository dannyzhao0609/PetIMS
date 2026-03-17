package com.petims.controller;

import com.petims.common.result.Result;
import com.petims.entity.HealthRecord;
import com.petims.service.HealthRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "健康记录管理", description = "健康记录管理接口")
@RestController
@RequestMapping("/health-records")
public class HealthRecordController {

    private final HealthRecordService healthRecordService;

    public HealthRecordController(HealthRecordService healthRecordService) {
        this.healthRecordService = healthRecordService;
    }

    @Operation(summary = "获取宠物的健康记录列表")
    @GetMapping("/pet/{petId}")
    public Result<List<HealthRecord>> listByPetId(@PathVariable Long petId) {
        return Result.success(healthRecordService.listByPetId(petId));
    }

    @Operation(summary = "根据ID获取健康记录")
    @GetMapping("/{id}")
    public Result<HealthRecord> getById(@PathVariable Long id) {
        return Result.success(healthRecordService.getById(id));
    }

    @Operation(summary = "新增健康记录")
    @PostMapping
    public Result<Void> save(@RequestBody HealthRecord healthRecord) {
        healthRecordService.save(healthRecord);
        return Result.success();
    }

    @Operation(summary = "更新健康记录")
    @PutMapping
    public Result<Void> updateById(@RequestBody HealthRecord healthRecord) {
        healthRecordService.updateById(healthRecord);
        return Result.success();
    }

    @Operation(summary = "删除健康记录")
    @DeleteMapping("/{id}")
    public Result<Void> removeById(@PathVariable Long id) {
        healthRecordService.removeById(id);
        return Result.success();
    }
}
