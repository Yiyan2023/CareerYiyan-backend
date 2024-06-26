package com.yiyan.careeryiyan.model.request;

import lombok.Data;

@Data
public class ModifyInfoRequest {
    private String blog;
    private String cv;
    private String education;
    private String email;
    private String enterpriseId;
    private String gender;
    private String github;
    private String id;
    private String interests;
    private String nickname;
    private String position;
    /**
     * 真实名字
     */
    private String username;
}
