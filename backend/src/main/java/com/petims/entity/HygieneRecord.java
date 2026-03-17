package com.petims.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("hygiene_records")
public class HygieneRecord extends BaseEntity {

    private Long petId;

    private String recordType;

    private LocalDate date;

    private String description;

    private String attachments;
}
