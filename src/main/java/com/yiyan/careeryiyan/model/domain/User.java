package com.yiyan.careeryiyan.model.domain;

import lombok.Data;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Data
public class User {
    private String id;
    private String username;
    private String nickname;
    private String password;
    private String email;
    private String gender;
    private String salt;
    private Date registerTime;
    private String avatarUrl;

    private String blog;
    private String cv;
    private String education;
    private String enterpriseId;
    private String enterpriseName;
    private String github;
    private String interests;
    private String position;



    public Map<String,Object> toDict(){
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("username", username);
        map.put("nickname", nickname);
        map.put("email", email);
        map.put("gender", gender);
        map.put("registerTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(registerTime));
        map.put("avatarUrl", avatarUrl);
        return map;
    }

}
