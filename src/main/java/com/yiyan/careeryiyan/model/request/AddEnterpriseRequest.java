package com.yiyan.careeryiyan.model.request;

import lombok.Data;

import java.util.Date;

@Data
public class AddEnterpriseRequest {
    private String epName;
    private String epAddr;
    private String epDesc;
    private String epType;
    private String epLicense;
    //待定补充

    private String epAvatarUrl;

}
