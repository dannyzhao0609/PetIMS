package com.petims.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("monitoring_records")
public class MonitoringRecord extends BaseEntity {

    private Long petId;

    private String recordType;

    private String value;

    private String unit;

    private LocalDateTime recordTime;
}
