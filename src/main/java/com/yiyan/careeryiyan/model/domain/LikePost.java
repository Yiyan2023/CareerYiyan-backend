package com.yiyan.careeryiyan.model.domain;

import lombok.Data;

import java.util.Date;

@Data
public class LikePost {
    private String likePostId;
    private Date likePostCreateAt;
    private String userId;
    private String postId;
    private boolean isDelete;

    public LikePost(String userId, String postId) {
        this.userId = userId;
        this.postId = postId;
        this.likePostCreateAt = new Date();
        this.isDelete = false;
    }
}
