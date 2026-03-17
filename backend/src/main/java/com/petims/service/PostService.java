package com.petims.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.petims.entity.Post;
import com.petims.mapper.PostMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService extends ServiceImpl<PostMapper, Post> {

    public List<Post> listRecent() {
        QueryWrapper<Post> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("create_time");
        wrapper.last("LIMIT 20");
        return list(wrapper);
    }

    public List<Post> listByUserId(Long userId) {
        QueryWrapper<Post> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        wrapper.orderByDesc("create_time");
        return list(wrapper);
    }
}
