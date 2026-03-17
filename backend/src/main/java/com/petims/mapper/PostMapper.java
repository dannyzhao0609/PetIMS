package com.petims.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.petims.entity.Post;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PostMapper extends BaseMapper<Post> {
}
