package com.petims.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("health_records")
public class HealthRecord extends BaseEntity {

    private Long petId;

    private String recordType;

    private String title;

    private String description;

    private LocalDate date;

    private String attachments;
}
