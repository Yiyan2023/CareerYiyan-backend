package com.yiyan.careeryiyan.model.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import java.text.SimpleDateFormat;
import java.util.*;

@Data
public class Post {
    private int postId;
    private String postTitle;
    private String postContent;
    private Date postCreateAt;
    private int userId;
    private  String postPhotoUrls;
    private int postParentId;
    private boolean isDelete=false;

    public Post( String postContent, int userId,String photos) {
        this.postContent = postContent;
        this.postCreateAt = new Date();
        this.userId = userId;
        this.postPhotoUrls = photos;
    }


    public Map<String, Object> toDict() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", postId);
        map.put("postContent", postContent);
        map.put("postCreatedAt", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(postCreateAt));
        map.put("postPhotoUrls", this.postPhotoUrls);  // 使用 getter 确保 photos 是最新的
        return map;
    }
}