package com.petims.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.petims.entity.HygieneRecord;
import com.petims.mapper.HygieneRecordMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HygieneRecordService extends ServiceImpl<HygieneRecordMapper, HygieneRecord> {

    public List<HygieneRecord> listByPetId(Long petId) {
        QueryWrapper<HygieneRecord> wrapper = new QueryWrapper<>();
        wrapper.eq("pet_id", petId);
        wrapper.orderByDesc("date");
        return list(wrapper);
    }
}
