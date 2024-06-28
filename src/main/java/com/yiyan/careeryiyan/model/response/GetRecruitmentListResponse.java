package com.yiyan.careeryiyan.model.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.yiyan.careeryiyan.model.domain.Recruitment;
import com.yiyan.careeryiyan.model.domain.User;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GetRecruitmentListResponse {
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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime createTime;
    private int headCount;
    private int offerCount;

    private String hrId;
    private String hrName;
    private String hrAvatarUrl;
    private String gender;

    public GetRecruitmentListResponse() {}
    public GetRecruitmentListResponse(Recruitment recruitment, User hr){
//        this.setId(recruitment.getId());
//        this.setEnterpriseId(recruitment.getEnterpriseId());
//        this.setRecruitmentName(recruitment.getRecruitmentName());
//        this.setRecruitmentAddress(recruitment.getRecruitmentAddress());
//        this.setRecruitmentTag(recruitment.getRecruitmentTag());
//        this.setMinSalary(recruitment.getMinSalary());
//        this.setMaxSalary(recruitment.getMaxSalary());
//        this.setSalaryInterval(recruitment.getSalaryInterval());
//        this.setEducation(recruitment.getEducation());
//        this.setRecruitmentDescription(recruitment.getRecruitmentDescription());
//        this.setCreateTime(recruitment.getCreateTime());
//        this.setHeadCount(recruitment.getHeadCount());
//        this.setOfferCount(recruitment.getOfferCount());
//
//        this.setHrId(hr.getUserId());
//        this.setHrName(hr.getUserName());
//        this.setHrAvatarUrl(hr.getUserAvatarUrl());
//        this.setGender(hr.getUserGender());
    }
}
