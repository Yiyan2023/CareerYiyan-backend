package com.yiyan.careeryiyan.model.domain;

import lombok.Data;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Data
public class Comment {
    private String commentId;
    private String postId;
    private String userId;
    private String commentParentId;
    private String commentContent;
    private Date commentCreateAt;
    private boolean isDelete;

    public Comment(String postId, String userId, String commentContent, String commentParentId) {
        this.postId = postId;
        this.userId = userId;
        this.commentContent = commentContent;
        this.commentCreateAt = new Date();
        this.commentParentId = commentParentId;
        this.isDelete = false;
    }

    public Map<String, Object> toDict() {
        Map<String, Object> map = new HashMap<>();
        map.put("commentId", commentId);
        map.put("commentContent", commentContent);
        map.put("commentCreateAt", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(commentCreateAt));
//        map.put("isDelete", isDelete);
        return map;
    }
}
