package com.petims.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@TableName("location_tracks")
public class LocationTrack extends BaseEntity {

    private Long petId;

    private Long sensorId;

    private BigDecimal latitude;

    private BigDecimal longitude;

    private String address;

    private LocalDateTime trackTime;

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

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LocalDateTime getTrackTime() {
        return trackTime;
    }

    public void setTrackTime(LocalDateTime trackTime) {
        this.trackTime = trackTime;
    }
}
