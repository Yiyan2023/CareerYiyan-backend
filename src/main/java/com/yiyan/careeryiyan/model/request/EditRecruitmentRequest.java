package com.yiyan.careeryiyan.model.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EditRecruitmentRequest {
    private String id;
    private String enterpriseId;
    private String recruitmentName;
    private String recruitmentAddress;
    private String recruitmentTag;
    private int minSalary;
    private int maxSalary;
    private int salaryInterval;
    private String education;
    private String recruitmentDescription;
    private int headCount;
    private int offerCount;
}

