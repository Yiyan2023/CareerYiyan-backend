package com.yiyan.careeryiyan.model.domain;

import lombok.Data;

import java.util.Date;

@Data
public class LikeComment {
    private int likeCommentId;
    private Date likeCommentCreateAt;
    private int userId;
    private int commentId;
    private boolean isDelete;

    public LikeComment(int userId, int commentId) {
        this.userId = userId;
        this.commentId = commentId;
        this.likeCommentCreateAt = new Date();
        this.isDelete = false;
    }
}
