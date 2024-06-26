package com.yiyan.careeryiyan.model.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;

import java.time.LocalDateTime;

//返回用户的投递详情

@Data
public class UserApplyDetailResponse {
    //apply
    private String id;
    private String userId;
    private String recruitmentId;
    private int status;
    private String cvUrl;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private  LocalDateTime createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private  LocalDateTime updateTime;

    //recruitment
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

    //enterprise
    private String enterpriseName;
    private String enterpriseAddress;
    private String enterpriseDescription;
    private String enterpriseType;
    private String enterpriseLicense;
    private String avatarUrl;
}
