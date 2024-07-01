package com.yiyan.careeryiyan.model.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Data
public class User {
    private String userId;
    private String userName;
    @JsonIgnore
    private String userPwd;
    private String userEmail;
    private String userGender;
    @JsonIgnore
    private String userSalt;
    private Date userRegAt;
    private String userAvatarUrl;
    private String userNickname;
    private String userEdu;
    private String userInterest;
    private String userCvUrl;
    private String userAddr;
    private String userGithubUrl;
    private String userBlogUrl;
    private int userInfluence;
    private int isDelete;

    public User() {
        this.userRegAt = new Date();
        this.userInfluence = 0;
        this.isDelete = 0;
    }

    public Map<String, Object> toDict() {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("userName", userName);
        map.put("userNickname", userNickname);
        map.put("userEmail", userEmail);
        map.put("userGender", userGender);
        map.put("userRegAt", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(userRegAt));
        map.put("userAvatarUrl", userAvatarUrl);
        return map;
    }
}
