package com.petims.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.petims.entity.FeedingRecord;
import com.petims.mapper.FeedingRecordMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeedingRecordService extends ServiceImpl<FeedingRecordMapper, FeedingRecord> {

    public List<FeedingRecord> listByPetId(Long petId) {
        QueryWrapper<FeedingRecord> wrapper = new QueryWrapper<>();
        wrapper.eq("pet_id", petId);
        wrapper.orderByDesc("feeding_time");
        return list(wrapper);
    }
}
