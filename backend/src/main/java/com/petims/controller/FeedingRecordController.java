package com.petims.controller;

import com.petims.common.result.Result;
import com.petims.entity.FeedingRecord;
import com.petims.service.FeedingRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "喂食记录管理", description = "喂食记录管理接口")
@RestController
@RequestMapping("/feeding-records")
public class FeedingRecordController {

    private final FeedingRecordService feedingRecordService;

    public FeedingRecordController(FeedingRecordService feedingRecordService) {
        this.feedingRecordService = feedingRecordService;
    }

    @Operation(summary = "获取宠物的喂食记录列表")
    @GetMapping("/pet/{petId}")
    public Result<List<FeedingRecord>> listByPetId(@PathVariable Long petId) {
        return Result.success(feedingRecordService.listByPetId(petId));
    }

    @Operation(summary = "根据ID获取喂食记录")
    @GetMapping("/{id}")
    public Result<FeedingRecord> getById(@PathVariable Long id) {
        return Result.success(feedingRecordService.getById(id));
    }

    @Operation(summary = "新增喂食记录")
    @PostMapping
    public Result<Void> save(@RequestBody FeedingRecord feedingRecord) {
        feedingRecordService.save(feedingRecord);
        return Result.success();
    }

    @Operation(summary = "更新喂食记录")
    @PutMapping
    public Result<Void> updateById(@RequestBody FeedingRecord feedingRecord) {
        feedingRecordService.updateById(feedingRecord);
        return Result.success();
    }

    @Operation(summary = "删除喂食记录")
    @DeleteMapping("/{id}")
    public Result<Void> removeById(@PathVariable Long id) {
        feedingRecordService.removeById(id);
        return Result.success();
    }
}
