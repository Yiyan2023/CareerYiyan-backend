package com.yiyan.careeryiyan.model.domain;

import lombok.Data;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Data
public class Comment {
    private int commentId;
    private int postId;
    private int userId;
    private int commentParentId;
    private String commentContent;
    private Date commentCreatedAt;
    private boolean isDelete;

    public Comment(int postId, int userId, String commentContent, int commentParentId) {
        this.postId = postId;
        this.userId = userId;
        this.commentContent = commentContent;
        this.commentCreatedAt = new Date();
        this.commentParentId = commentParentId;
        this.isDelete = false;
    }

    public Map<String, Object> toDict() {
        Map<String, Object> map = new HashMap<>();
        map.put("commentId", commentId);
        map.put("commentContent", commentContent);
        map.put("commentCreatedAt", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(commentCreatedAt));
        map.put("isDelete", isDelete);
        return map;
    }
}
