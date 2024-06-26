package com.yiyan.careeryiyan.model.domain;

import lombok.Data;

import java.lang.annotation.Native;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Data
public class Comment {
    private String id;
    private String postId;
    private String parentId;
    private String userId;
    private String content;
    private Date createdAt;
    public Comment(String postId, String userId, String content,String parentId){
        this.postId = postId;
        this.userId = userId;
        this.content = content;
        this.createdAt = new Date();
        this.parentId = parentId;
    }
    public Map<String,Object>toDict(){
        Map<String, Object> map = new HashMap<>();
        map.put("id",id);
        map.put("content",content);
        map.put("createdAt",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(createdAt));
        return map;
    }
}
