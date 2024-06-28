package com.yiyan.careeryiyan.model.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.yiyan.careeryiyan.model.domain.Enterprise;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EnterpriseInfoResponse {
    private String id;
    private String enterpriseName;
    private String enterpriseAddress;
    private String enterpriseDescription;
    private String enterpriseType;
    private String enterpriseLicense;
    //待定补充
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime createTime;
    private String avatarUrl;

    private int auth;//0非企业员工 1普通员工 2管理员

    public EnterpriseInfoResponse() {}
    public EnterpriseInfoResponse(Enterprise enterprise,int auth){
//        this.id = enterprise.getId();
//        this.enterpriseName = enterprise.getEnterpriseName();
//        this.enterpriseAddress = enterprise.getEnterpriseAddress();
//        this.enterpriseDescription = enterprise.getEnterpriseDescription();
//        this.enterpriseType = enterprise.getEnterpriseType();
//        this.enterpriseLicense = enterprise.getEnterpriseLicense();
//        this.createTime = enterprise.getCreateTime();
//        this.avatarUrl = enterprise.getAvatarUrl();
//        this.auth = auth;
    }
}
