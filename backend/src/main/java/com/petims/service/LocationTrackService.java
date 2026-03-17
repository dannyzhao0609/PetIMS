package com.petims.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.petims.entity.LocationTrack;
import com.petims.mapper.LocationTrackMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationTrackService extends ServiceImpl<LocationTrackMapper, LocationTrack> {

    public List<LocationTrack> listByPetId(Long petId) {
        QueryWrapper<LocationTrack> wrapper = new QueryWrapper<>();
        wrapper.eq("pet_id", petId);
        wrapper.orderByDesc("track_time");
        wrapper.last("LIMIT 100");
        return list(wrapper);
    }

    public LocationTrack getLatestByPetId(Long petId) {
        QueryWrapper<LocationTrack> wrapper = new QueryWrapper<>();
        wrapper.eq("pet_id", petId);
        wrapper.orderByDesc("track_time");
        wrapper.last("LIMIT 1");
        return getOne(wrapper);
    }
}
