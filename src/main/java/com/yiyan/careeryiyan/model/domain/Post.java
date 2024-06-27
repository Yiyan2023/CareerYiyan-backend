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
    private String id;
    private String title;
    private String content;
    private Date createdAt;
    private String userId;
    private boolean isPrivate;
    private String photos;
    private String parentId;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public Post( String content, String userId,String photos) {
        this.content = content;
        this.createdAt = new Date();
        this.userId = userId;
        this.photos = photos;
    }

//    public void addPhoto(String url) {
//        if (this.photos == null) {
//            this.photos = new ArrayList<>();
//        }
//        this.photos.add(url);
//        updatePhotosJson();
//    }
//
//    @JsonIgnore
//    public String getPhotosJson() {
//        if (photosJson == null) {
//            updatePhotosJson();
//        }
//        return photosJson;
//    }
//
//    public void setPhotosJson(String photosJson) {
//        this.photosJson = photosJson;
//        updatePhotosList();
//    }
//
//    public List<String> getPhotos() {
//        if (photos == null) {
//            updatePhotosList();
//        }
//        return photos;
//    }
//
//    public void setPhotos(List<String> photos) {
//        this.photos = photos;
//        updatePhotosJson();
//    }
//
//    private void updatePhotosJson() {
//        try {
//            this.photosJson = objectMapper.writeValueAsString(this.photos);
//        } catch (JsonProcessingException e) {
//            // 处理异常，可以记录日志或者抛出自定义异常
//            this.photosJson = "[]";
//        }
//    }
//
//    private void updatePhotosList() {
//        try {
//            if (this.photosJson == null || this.photosJson.isEmpty()) {
//                this.photos = new ArrayList<>();
//            } else {
//                this.photos = objectMapper.readValue(this.photosJson, new TypeReference<List<String>>() {});
//            }
//        } catch (JsonProcessingException e) {
//            // 处理异常，可以记录日志或者抛出自定义异常
//            this.photos = new ArrayList<>();
//        }
//    }

    public Map<String, Object> toDict() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("content", content);
        map.put("createdAt", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(createdAt));
        map.put("photos", getPhotos());  // 使用 getter 确保 photos 是最新的
        map.put("origin", new HashMap<>());
        map.put("title",title);
        return map;
    }
}