package com.petims.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDate;

@TableName("hygiene_records")
public class HygieneRecord extends BaseEntity {

    private Long petId;

    private String recordType;

    private LocalDate date;

    private String description;

    private String attachments;

    public Long getPetId() {
        return petId;
    }

    public void setPetId(Long petId) {
        this.petId = petId;
    }

    public String getRecordType() {
        return recordType;
    }

    public void setRecordType(String recordType) {
        this.recordType = recordType;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAttachments() {
        return attachments;
    }

    public void setAttachments(String attachments) {
        this.attachments = attachments;
    }
}
