package com.yiyan.careeryiyan.model.domain;

import lombok.Data;

import java.util.Date;

@Data
public class FollowEnterprise {
    private Integer followEpId;
    private Integer epId;
    private Integer userId;
    private Date followEpCreateAt;
    private Boolean isDelete;

    public FollowEnterprise(Integer epId, Integer userId) {
        this.epId = epId;
        this.userId = userId;
        this.followEpCreateAt = new Date();
        this.isDelete = false;
    }

    // Getters and Setters
}
