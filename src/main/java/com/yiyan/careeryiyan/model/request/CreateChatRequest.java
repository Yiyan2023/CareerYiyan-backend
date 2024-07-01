package com.yiyan.careeryiyan.model.request;

import lombok.Data;

@Data
public class CreateChatRequest {
    private String chatUserId1;//自己
    private String chatUserId2;//对方
}
