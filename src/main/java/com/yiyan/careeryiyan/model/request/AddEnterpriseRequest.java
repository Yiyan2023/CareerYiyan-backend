package com.yiyan.careeryiyan.model.request;

import lombok.Data;

import java.util.Date;

@Data
public class AddEnterpriseRequest {
    private String enterpriseName;
    private String enterpriseAddress;
    private String enterpriseDescription;
    private String enterpriseType;
    private String enterpriseLicense;
    //待定补充

    private String avatarUrl;

}
