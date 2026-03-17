package com.petims.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.petims.common.result.Result;
import com.petims.entity.LocationTrack;
import com.petims.service.LocationTrackService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "位置跟踪管理", description = "位置跟踪管理接口")
@RestController
@RequestMapping("/location-tracks")
public class LocationTrackController {

    private final LocationTrackService locationTrackService;

    public LocationTrackController(LocationTrackService locationTrackService) {
        this.locationTrackService = locationTrackService;
    }

    @Operation(summary = "获取所有位置跟踪列表")
    @GetMapping
    public Result<List<LocationTrack>> listAll() {
        QueryWrapper<LocationTrack> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("track_time");
        return Result.success(locationTrackService.list(wrapper));
    }

    @Operation(summary = "获取宠物的位置跟踪列表")
    @GetMapping("/pet/{petId}")
    public Result<List<LocationTrack>> listByPetId(@PathVariable Long petId) {
        return Result.success(locationTrackService.listByPetId(petId));
    }

    @Operation(summary = "获取宠物的最新位置")
    @GetMapping("/pet/{petId}/latest")
    public Result<LocationTrack> getLatestByPetId(@PathVariable Long petId) {
        return Result.success(locationTrackService.getLatestByPetId(petId));
    }

    @Operation(summary = "根据ID获取位置")
    @GetMapping("/{id}")
    public Result<LocationTrack> getById(@PathVariable Long id) {
        return Result.success(locationTrackService.getById(id));
    }

    @Operation(summary = "新增位置")
    @PostMapping
    public Result<Void> save(@RequestBody LocationTrack locationTrack) {
        locationTrackService.save(locationTrack);
        return Result.success();
    }

    @Operation(summary = "更新位置")
    @PutMapping
    public Result<Void> updateById(@RequestBody LocationTrack locationTrack) {
        locationTrackService.updateById(locationTrack);
        return Result.success();
    }

    @Operation(summary = "删除位置")
    @DeleteMapping("/{id}")
    public Result<Void> removeById(@PathVariable Long id) {
        locationTrackService.removeById(id);
        return Result.success();
    }
}
