package com.petims.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("pets")
public class Pet extends BaseEntity {

    private Long userId;

    private String name;

    private String species;

    private String breed;

    private String gender;

    private LocalDate birthday;

    private BigDecimal weight;

    private BigDecimal height;

    private String avatar;

    private String remarks;
}
