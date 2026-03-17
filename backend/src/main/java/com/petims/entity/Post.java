package com.petims.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("posts")
public class Post extends BaseEntity {

    private Long userId;

    private Long petId;

    private String title;

    private String content;

    private String attachments;

    private Integer likes;

    private Integer comments;
}
