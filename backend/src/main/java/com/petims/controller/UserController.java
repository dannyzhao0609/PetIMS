package com.petims.controller;

import com.petims.common.result.Result;
import com.petims.entity.User;
import com.petims.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@Tag(name = "用户管理", description = "用户管理接口")
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "根据ID获取用户信息")
    @GetMapping("/{id}")
    public Result<User> getById(@PathVariable Long id) {
        return Result.success(userService.getById(id));
    }

    @Operation(summary = "新增用户")
    @PostMapping
    public Result<Void> save(@RequestBody User user) {
        userService.save(user);
        return Result.success();
    }

    @Operation(summary = "更新用户信息")
    @PutMapping
    public Result<Void> updateById(@RequestBody User user) {
        userService.updateById(user);
        return Result.success();
    }

    @Operation(summary = "删除用户")
    @DeleteMapping("/{id}")
    public Result<Void> removeById(@PathVariable Long id) {
        userService.removeById(id);
        return Result.success();
    }
}
