package com.petims.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.petims.entity.Sensor;
import com.petims.mapper.SensorMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SensorService extends ServiceImpl<SensorMapper, Sensor> {

    public List<Sensor> listAll() {
        QueryWrapper<Sensor> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("create_time");
        return list(wrapper);
    }

    public List<Sensor> listByUserId(Long userId) {
        QueryWrapper<Sensor> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        wrapper.orderByDesc("create_time");
        return list(wrapper);
    }
}
