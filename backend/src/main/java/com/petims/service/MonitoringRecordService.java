package com.petims.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.petims.entity.MonitoringRecord;
import com.petims.mapper.MonitoringRecordMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MonitoringRecordService extends ServiceImpl<MonitoringRecordMapper, MonitoringRecord> {

    public List<MonitoringRecord> listByPetId(Long petId) {
        QueryWrapper<MonitoringRecord> wrapper = new QueryWrapper<>();
        wrapper.eq("pet_id", petId);
        wrapper.orderByDesc("record_time");
        return list(wrapper);
    }
}
