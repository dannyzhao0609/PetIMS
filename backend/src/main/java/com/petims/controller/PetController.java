package com.petims.controller;

import com.petims.common.result.Result;
import com.petims.entity.Pet;
import com.petims.service.PetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "宠物管理", description = "宠物管理接口")
@RestController
@RequestMapping("/pets")
public class PetController {

    private final PetService petService;

    public PetController(PetService petService) {
        this.petService = petService;
    }

    @Operation(summary = "获取用户的宠物列表")
    @GetMapping("/user/{userId}")
    public Result<List<Pet>> listByUserId(@PathVariable Long userId) {
        return Result.success(petService.listByUserId(userId));
    }

    @Operation(summary = "根据ID获取宠物信息")
    @GetMapping("/{id}")
    public Result<Pet> getById(@PathVariable Long id) {
        return Result.success(petService.getById(id));
    }

    @Operation(summary = "新增宠物")
    @PostMapping
    public Result<Void> save(@RequestBody Pet pet) {
        petService.save(pet);
        return Result.success();
    }

    @Operation(summary = "更新宠物信息")
    @PutMapping
    public Result<Void> updateById(@RequestBody Pet pet) {
        petService.updateById(pet);
        return Result.success();
    }

    @Operation(summary = "删除宠物")
    @DeleteMapping("/{id}")
    public Result<Void> removeById(@PathVariable Long id) {
        petService.removeById(id);
        return Result.success();
    }
}
