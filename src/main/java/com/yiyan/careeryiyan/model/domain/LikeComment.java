package com.yiyan.careeryiyan.model.domain;

import lombok.Data;

import java.util.Date;

@Data
public class LikeComment {
    private String likeCommentId;
    private Date likeCommentCreateAt;
    private String userId;
    private String commentId;
    private boolean isDelete;

    public LikeComment(String userId, String commentId) {
        this.userId = userId;
        this.commentId = commentId;
        this.likeCommentCreateAt = new Date();
        this.isDelete = false;
    }
}
