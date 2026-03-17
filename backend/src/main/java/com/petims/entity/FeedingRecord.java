package com.petims.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("feeding_records")
public class FeedingRecord extends BaseEntity {

    private Long petId;

    private String foodName;

    private BigDecimal amount;

    private String unit;

    private LocalDateTime feedingTime;

    private String notes;
}
