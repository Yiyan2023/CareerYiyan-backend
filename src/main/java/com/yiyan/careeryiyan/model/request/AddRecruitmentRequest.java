package com.yiyan.careeryiyan.model.request;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AddRecruitmentRequest {
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
    private LocalDateTime rcCreateAt;
}
