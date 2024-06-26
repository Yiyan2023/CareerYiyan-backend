package com.yiyan.careeryiyan.model.domain;

import lombok.Data;

import java.util.Date;

@Data
public class Like {
    private String id;
    private Date createdAt;
    private String userId;
    private String type;
    private String foreignId;

    public Like( String userId, String type, String foreignId){
        this.userId = userId;
        this.type = type;
        this.foreignId = foreignId;
        this.createdAt=new Date();

    }
}
