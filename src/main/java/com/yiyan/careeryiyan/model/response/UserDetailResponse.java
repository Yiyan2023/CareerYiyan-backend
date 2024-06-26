package com.yiyan.careeryiyan.model.response;

import lombok.Data;

import java.util.Date;

@Data
public class UserDetailResponse {
    private String userId;
    private String username;
    private String nickname;
    private String password;
    private String email;
    private String gender;
    private String salt;
    private Date registerTime;
    private String userAvatarUrl;

    private String blog;
    private String cv;
    private String education;
    private String enterpriseName;
    private String github;
    private String interests;
    private String position;

    private String enterpriseAddress;
    private String enterpriseDescription;
    private String enterpriseType;
    private String enterpriseLicense;;
    private String enterpriseAvatarUrl;
}
