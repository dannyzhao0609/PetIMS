package com.petims.controller;

import com.petims.common.result.Result;
import com.petims.entity.Sensor;
import com.petims.service.SensorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "传感器管理", description = "传感器管理接口")
@RestController
@RequestMapping("/sensors")
public class SensorController {

    private final SensorService sensorService;

    public SensorController(SensorService sensorService) {
        this.sensorService = sensorService;
    }

    @Operation(summary = "获取所有传感器列表")
    @GetMapping
    public Result<List<Sensor>> listAll() {
        return Result.success(sensorService.listAll());
    }

    @Operation(summary = "获取用户的传感器列表")
    @GetMapping("/user/{userId}")
    public Result<List<Sensor>> listByUserId(@PathVariable Long userId) {
        return Result.success(sensorService.listByUserId(userId));
    }

    @Operation(summary = "根据ID获取传感器")
    @GetMapping("/{id}")
    public Result<Sensor> getById(@PathVariable Long id) {
        return Result.success(sensorService.getById(id));
    }

    @Operation(summary = "新增传感器")
    @PostMapping
    public Result<Void> save(@RequestBody Sensor sensor) {
        if (sensor.getUserId() == null) {
            sensor.setUserId(1L);
        }
        sensorService.save(sensor);
        return Result.success();
    }

    @Operation(summary = "更新传感器")
    @PutMapping
    public Result<Void> updateById(@RequestBody Sensor sensor) {
        sensorService.updateById(sensor);
        return Result.success();
    }

    @Operation(summary = "删除传感器")
    @DeleteMapping("/{id}")
    public Result<Void> removeById(@PathVariable Long id) {
        sensorService.removeById(id);
        return Result.success();
    }
}
