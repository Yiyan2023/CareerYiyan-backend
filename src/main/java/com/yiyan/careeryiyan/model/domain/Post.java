package com.yiyan.careeryiyan.model.domain;

import lombok.Data;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Data
public class Post {
    private  String id;
    private String title;
    private  String content;
    private Date createdAt;
    private String userId;
    private boolean isPrivate;

    public Map<String,Object> toDict(){
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("title", title);
        map.put("content", content);
        map.put("createdAt", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(createdAt));
        return map;
    }

}
