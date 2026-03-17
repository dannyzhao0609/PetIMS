package com.petims.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@TableName("feeding_records")
public class FeedingRecord extends BaseEntity {

    private Long petId;

    private String foodName;

    private BigDecimal amount;

    private String unit;

    private LocalDateTime feedingTime;

    private String notes;

    public Long getPetId() {
        return petId;
    }

    public void setPetId(Long petId) {
        this.petId = petId;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public LocalDateTime getFeedingTime() {
        return feedingTime;
    }

    public void setFeedingTime(LocalDateTime feedingTime) {
        this.feedingTime = feedingTime;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
