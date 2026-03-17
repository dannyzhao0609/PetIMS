package com.petims.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.petims.entity.HealthRecord;
import com.petims.mapper.HealthRecordMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HealthRecordService extends ServiceImpl<HealthRecordMapper, HealthRecord> {

    public List<HealthRecord> listByPetId(Long petId) {
        QueryWrapper<HealthRecord> wrapper = new QueryWrapper<>();
        wrapper.eq("pet_id", petId);
        wrapper.orderByDesc("date");
        return list(wrapper);
    }
}
