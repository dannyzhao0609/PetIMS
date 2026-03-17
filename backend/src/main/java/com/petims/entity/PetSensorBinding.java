package com.petims.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;

@TableName("pet_sensor_bindings")
public class PetSensorBinding extends BaseEntity {

    private Long petId;

    private Long sensorId;

    private LocalDateTime bindTime;

    private LocalDateTime unbindTime;

    private String status;

    public Long getPetId() {
        return petId;
    }

    public void setPetId(Long petId) {
        this.petId = petId;
    }

    public Long getSensorId() {
        return sensorId;
    }

    public void setSensorId(Long sensorId) {
        this.sensorId = sensorId;
    }

    public LocalDateTime getBindTime() {
        return bindTime;
    }

    public void setBindTime(LocalDateTime bindTime) {
        this.bindTime = bindTime;
    }

    public LocalDateTime getUnbindTime() {
        return unbindTime;
    }

    public void setUnbindTime(LocalDateTime unbindTime) {
        this.unbindTime = unbindTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
