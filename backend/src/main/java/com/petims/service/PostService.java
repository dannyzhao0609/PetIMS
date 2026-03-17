package com.petims.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.petims.dto.PostDTO;
import com.petims.entity.Post;
import com.petims.entity.User;
import com.petims.mapper.PostMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PostService extends ServiceImpl<PostMapper, Post> {

    private final UserService userService;

    public PostService(UserService userService) {
        this.userService = userService;
    }

    public List<PostDTO> listAllDTO() {
        QueryWrapper<Post> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("create_time");
        List<Post> posts = list(wrapper);
        return convertToDTOList(posts);
    }

    public List<PostDTO> listRecentDTO() {
        QueryWrapper<Post> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("create_time");
        wrapper.last("LIMIT 20");
        List<Post> posts = list(wrapper);
        return convertToDTOList(posts);
    }

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

    private List<PostDTO> convertToDTOList(List<Post> posts) {
        if (posts.isEmpty()) {
            return new ArrayList<>();
        }

        List<Long> userIds = posts.stream().map(Post::getUserId).distinct().collect(Collectors.toList());
        List<User> users = userService.listByIds(userIds);
        Map<Long, User> userMap = users.stream().collect(Collectors.toMap(User::getId, u -> u));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        return posts.stream().map(post -> {
            PostDTO dto = new PostDTO();
            BeanUtils.copyProperties(post, dto);
            
            User user = userMap.get(post.getUserId());
            if (user != null) {
                dto.setUserName(user.getUsername());
            }
            
            if (post.getCreateTime() != null) {
                dto.setCreateTime(post.getCreateTime().format(formatter));
            }
            
            return dto;
        }).collect(Collectors.toList());
    }
}
