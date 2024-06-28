package com.yiyan.careeryiyan.model.domain;

import lombok.Data;

import java.util.Date;

@Data
public class FollowUser {
    private Integer followUserId;
    private Integer followingUserId;
    private Integer userId;
    private Date followUserCreateAt;
    private Boolean isDelete;

    public FollowUser(Integer followingUserId, Integer userId) {
        this.followingUserId = followingUserId;
        this.userId = userId;
        this.followUserCreateAt = new Date();
        this.isDelete = false;
    }

    // Getters and Setters
}
