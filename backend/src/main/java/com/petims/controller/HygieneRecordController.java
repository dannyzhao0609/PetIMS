package com.petims.controller;

import com.petims.common.result.Result;
import com.petims.entity.HygieneRecord;
import com.petims.service.HygieneRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "卫生记录管理", description = "卫生记录管理接口")
@RestController
@RequestMapping("/hygiene-records")
public class HygieneRecordController {

    private final HygieneRecordService hygieneRecordService;

    public HygieneRecordController(HygieneRecordService hygieneRecordService) {
        this.hygieneRecordService = hygieneRecordService;
    }

    @Operation(summary = "获取宠物的卫生记录列表")
    @GetMapping("/pet/{petId}")
    public Result<List<HygieneRecord>> listByPetId(@PathVariable Long petId) {
        return Result.success(hygieneRecordService.listByPetId(petId));
    }

    @Operation(summary = "根据ID获取卫生记录")
    @GetMapping("/{id}")
    public Result<HygieneRecord> getById(@PathVariable Long id) {
        return Result.success(hygieneRecordService.getById(id));
    }

    @Operation(summary = "新增卫生记录")
    @PostMapping
    public Result<Void> save(@RequestBody HygieneRecord hygieneRecord) {
        hygieneRecordService.save(hygieneRecord);
        return Result.success();
    }

    @Operation(summary = "更新卫生记录")
    @PutMapping
    public Result<Void> updateById(@RequestBody HygieneRecord hygieneRecord) {
        hygieneRecordService.updateById(hygieneRecord);
        return Result.success();
    }

    @Operation(summary = "删除卫生记录")
    @DeleteMapping("/{id}")
    public Result<Void> removeById(@PathVariable Long id) {
        hygieneRecordService.removeById(id);
        return Result.success();
    }
}
