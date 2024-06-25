package com.yiyan.careeryiyan.model.domain;

import lombok.Data;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Data
public class Enterprise {
    private String id;
    private String enterpriseName;
    private String enterpriseAddress;
    private String enterpriseDescription;
    private String enterpriseType;
    private String enterpriseLicense;
    //待定补充

    private Date createTime;
    private String avatarUrl;

}
