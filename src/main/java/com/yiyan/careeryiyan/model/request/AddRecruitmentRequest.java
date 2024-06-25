package com.yiyan.careeryiyan.model.request;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AddRecruitmentRequest {
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
    private LocalDateTime createTime;
}
