package com.yiyan.careeryiyan.model.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;

import java.time.LocalDateTime;

@Data

public class Recruitment {
    private String epId;
    private int isDelete;
    private int rcAcceptCount;
    private String rcAddr;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime rcCreateAt;
    private String rcDesc;
    private String rcEdu;
    private String rcId;
    private int rcMaxSalary;
    private int rcMinSalary;
    private String rcName;
    private int rcOfferCount;
    private int rcSalaryCount;
    private String rcTag;
    private int rcTotalCount;
}
