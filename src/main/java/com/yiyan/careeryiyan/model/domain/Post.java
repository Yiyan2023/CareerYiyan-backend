package com.yiyan.careeryiyan.model.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.NullSerializer;
import lombok.Data;

import java.text.SimpleDateFormat;
import java.util.*;

@Data
public class Post {
    private String postId;
    private String postTitle;
    private String postContent;
    private Date postCreateAt;
    private String userId;
    private String postPhotoUrls;
    @JsonSerialize(nullsUsing = NullSerializer.class)
    private String postParentId;
    private boolean isDelete=false;

    public Post(String postContent, String userId, String photos, String postTitle, String postParentId) {
        this.postContent = postContent;
        this.postCreateAt = new Date();
        this.userId = userId;
        this.postPhotoUrls = photos;
        this.postTitle = postTitle;
        this.postParentId = postParentId;
    }


    public Map<String, Object> toDict() {
        Map<String, Object> map = new HashMap<>();
        map.put("postId", postId);
        map.put("postContent", postContent);
        map.put("postCreatedAt", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(postCreateAt));
        map.put("postPhotoUrls", this.postPhotoUrls);  // 使用 getter 确保 photos 是最新的
//        map.put("postParentId", this.postParentId);
        map.put("postTitle", this.postTitle);
        return map;
    }
}