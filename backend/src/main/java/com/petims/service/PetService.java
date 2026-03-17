package com.petims.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.petims.entity.Pet;
import com.petims.mapper.PetMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PetService extends ServiceImpl<PetMapper, Pet> {

    public List<Pet> listByUserId(Long userId) {
        QueryWrapper<Pet> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        wrapper.orderByDesc("create_time");
        return list(wrapper);
    }
}
