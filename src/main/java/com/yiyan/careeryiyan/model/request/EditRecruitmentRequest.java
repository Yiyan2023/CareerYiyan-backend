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
    private String rcId;
    private String epId;
    private String rcName;
    private String rcAddr;
    private String rcTag;
    private int rcMinSalary;
    private int rcMaxSalary;
    private int rcSalaryCount;
    private String rcEdu;
    private String rcDesc;
    private int rcTotalCount;
    private int rcOfferCount;
}

