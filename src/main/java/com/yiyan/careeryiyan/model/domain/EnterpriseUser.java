package com.yiyan.careeryiyan.model.domain;

import lombok.Data;

import java.util.Date;

@Data
public class EnterpriseUser {
    private String id;
    private String enterpriseId;
    private String userId;
    private int role;//0是管理员，1是员工
    private Date createTime;
    private int isDeleted;
}
