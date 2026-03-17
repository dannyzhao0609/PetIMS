package com.petims.controller;

import com.petims.common.result.Result;
import com.petims.entity.User;
import com.petims.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Tag(name = "用户管理", description = "用户管理接口")
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @Operation(summary = "用户登录")
    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody User loginUser) {
        User user = userService.getByUsername(loginUser.getUsername());
        if (user == null) {
            return Result.error("用户不存在");
        }
        
        String inputPassword = loginUser.getPassword();
        String storedPassword = user.getPassword();
        
        boolean passwordMatch = false;
        if (storedPassword != null && storedPassword.startsWith("$2a$")) {
            passwordMatch = passwordEncoder.matches(inputPassword, storedPassword);
        } else {
            passwordMatch = storedPassword != null && storedPassword.equals(inputPassword);
        }
        
        if (!passwordMatch) {
            return Result.error("密码错误");
        }
        Map<String, Object> data = new HashMap<>();
        data.put("token", "token_" + user.getId());
        data.put("userInfo", user);
        return Result.success(data);
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
