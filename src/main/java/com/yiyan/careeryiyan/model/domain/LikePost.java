package com.yiyan.careeryiyan.model.domain;

import lombok.Data;

import java.util.Date;

@Data
public class LikePost {
    private int likePostId;
    private Date likePostCreateAt;
    private int userId;
    private int postId;
    private boolean isDelete;

    public LikePost(int userId, int postId) {
        this.userId = userId;
        this.postId = postId;
        this.likePostCreateAt = new Date();
        this.isDelete = false;
    }
}
