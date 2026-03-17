package com.petims.controller;

import com.petims.common.result.Result;
import com.petims.entity.MonitoringRecord;
import com.petims.service.MonitoringRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "监控记录管理", description = "监控记录管理接口")
@RestController
@RequestMapping("/monitoring-records")
public class MonitoringRecordController {

    private final MonitoringRecordService monitoringRecordService;

    public MonitoringRecordController(MonitoringRecordService monitoringRecordService) {
        this.monitoringRecordService = monitoringRecordService;
    }

    @Operation(summary = "获取宠物的监控记录列表")
    @GetMapping("/pet/{petId}")
    public Result<List<MonitoringRecord>> listByPetId(@PathVariable Long petId) {
        return Result.success(monitoringRecordService.listByPetId(petId));
    }

    @Operation(summary = "根据ID获取监控记录")
    @GetMapping("/{id}")
    public Result<MonitoringRecord> getById(@PathVariable Long id) {
        return Result.success(monitoringRecordService.getById(id));
    }

    @Operation(summary = "新增监控记录")
    @PostMapping
    public Result<Void> save(@RequestBody MonitoringRecord monitoringRecord) {
        monitoringRecordService.save(monitoringRecord);
        return Result.success();
    }

    @Operation(summary = "更新监控记录")
    @PutMapping
    public Result<Void> updateById(@RequestBody MonitoringRecord monitoringRecord) {
        monitoringRecordService.updateById(monitoringRecord);
        return Result.success();
    }

    @Operation(summary = "删除监控记录")
    @DeleteMapping("/{id}")
    public Result<Void> removeById(@PathVariable Long id) {
        monitoringRecordService.removeById(id);
        return Result.success();
    }
}
