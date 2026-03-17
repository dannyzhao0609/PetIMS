package com.petims.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.petims.dto.PetSensorBindingDTO;
import com.petims.entity.Pet;
import com.petims.entity.PetSensorBinding;
import com.petims.entity.Sensor;
import com.petims.mapper.PetSensorBindingMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PetSensorBindingService extends ServiceImpl<PetSensorBindingMapper, PetSensorBinding> {

    private final PetService petService;
    private final SensorService sensorService;

    public PetSensorBindingService(PetService petService, SensorService sensorService) {
        this.petService = petService;
        this.sensorService = sensorService;
    }

    public List<PetSensorBindingDTO> listAllDTO() {
        QueryWrapper<PetSensorBinding> wrapper = new QueryWrapper<>();
        wrapper.eq("status", "bound");
        wrapper.orderByDesc("bind_time");
        List<PetSensorBinding> bindings = list(wrapper);
        return convertToDTOList(bindings);
    }

    public List<PetSensorBindingDTO> listByPetIdDTO(Long petId) {
        QueryWrapper<PetSensorBinding> wrapper = new QueryWrapper<>();
        wrapper.eq("pet_id", petId);
        wrapper.eq("status", "bound");
        wrapper.orderByDesc("bind_time");
        List<PetSensorBinding> bindings = list(wrapper);
        return convertToDTOList(bindings);
    }

    public List<PetSensorBinding> listByPetId(Long petId) {
        QueryWrapper<PetSensorBinding> wrapper = new QueryWrapper<>();
        wrapper.eq("pet_id", petId);
        wrapper.eq("status", "bound");
        wrapper.orderByDesc("bind_time");
        return list(wrapper);
    }

    public List<PetSensorBinding> listBySensorId(Long sensorId) {
        QueryWrapper<PetSensorBinding> wrapper = new QueryWrapper<>();
        wrapper.eq("sensor_id", sensorId);
        wrapper.eq("status", "bound");
        wrapper.orderByDesc("bind_time");
        return list(wrapper);
    }

    private List<PetSensorBindingDTO> convertToDTOList(List<PetSensorBinding> bindings) {
        if (bindings.isEmpty()) {
            return new ArrayList<>();
        }

        List<Long> petIds = bindings.stream().map(PetSensorBinding::getPetId).distinct().collect(Collectors.toList());
        List<Long> sensorIds = bindings.stream().map(PetSensorBinding::getSensorId).distinct().collect(Collectors.toList());

        List<Pet> pets = petService.listByIds(petIds);
        List<Sensor> sensors = sensorService.listByIds(sensorIds);

        Map<Long, Pet> petMap = pets.stream().collect(Collectors.toMap(Pet::getId, p -> p));
        Map<Long, Sensor> sensorMap = sensors.stream().collect(Collectors.toMap(Sensor::getId, s -> s));

        return bindings.stream().map(binding -> {
            PetSensorBindingDTO dto = new PetSensorBindingDTO();
            BeanUtils.copyProperties(binding, dto);
            
            Pet pet = petMap.get(binding.getPetId());
            if (pet != null) {
                dto.setPetName(pet.getName());
            }
            
            Sensor sensor = sensorMap.get(binding.getSensorId());
            if (sensor != null) {
                dto.setSensorName(sensor.getSensorName());
                dto.setSensorCode(sensor.getSensorCode());
            }
            
            return dto;
        }).collect(Collectors.toList());
    }
}
