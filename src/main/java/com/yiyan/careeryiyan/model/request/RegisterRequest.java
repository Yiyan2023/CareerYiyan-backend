package com.yiyan.careeryiyan.model.request;

import lombok.Data;

@Data
public class RegisterRequest {
    private String userName;
    private String userPwd;
    private String userEmail;
    private String userAvatarUrl;
    private String userSalt;
}