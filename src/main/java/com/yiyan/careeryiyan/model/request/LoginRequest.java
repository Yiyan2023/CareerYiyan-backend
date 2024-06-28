package com.yiyan.careeryiyan.model.request;

import lombok.Data;

@Data
public class LoginRequest {
    private String userEmail;
    private String userPwd;
}
