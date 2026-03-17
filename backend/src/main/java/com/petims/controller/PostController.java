package com.petims.controller;

import com.petims.common.result.Result;
import com.petims.entity.Post;
import com.petims.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "帖子管理", description = "帖子管理接口")
@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @Operation(summary = "获取最近的帖子列表")
    @GetMapping("/recent")
    public Result<List<Post>> listRecent() {
        return Result.success(postService.listRecent());
    }

    @Operation(summary = "获取用户的帖子列表")
    @GetMapping("/user/{userId}")
    public Result<List<Post>> listByUserId(@PathVariable Long userId) {
        return Result.success(postService.listByUserId(userId));
    }

    @Operation(summary = "根据ID获取帖子")
    @GetMapping("/{id}")
    public Result<Post> getById(@PathVariable Long id) {
        return Result.success(postService.getById(id));
    }

    @Operation(summary = "新增帖子")
    @PostMapping
    public Result<Void> save(@RequestBody Post post) {
        postService.save(post);
        return Result.success();
    }

    @Operation(summary = "更新帖子")
    @PutMapping
    public Result<Void> updateById(@RequestBody Post post) {
        postService.updateById(post);
        return Result.success();
    }

    @Operation(summary = "删除帖子")
    @DeleteMapping("/{id}")
    public Result<Void> removeById(@PathVariable Long id) {
        postService.removeById(id);
        return Result.success();
    }
}
