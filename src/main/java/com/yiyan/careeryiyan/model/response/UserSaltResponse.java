package com.yiyan.careeryiyan.model.response;

import lombok.Data;

@Data
public class UserSaltResponse {
    String salt;

    public UserSaltResponse(String salt){
        this.salt = salt;
    }
}
