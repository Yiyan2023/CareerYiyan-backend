package com.yiyan.careeryiyan.model.domain;

import lombok.Data;

@Data
public class Chat {
    private String chatId;
    private String chatUserId1;
    private String chatUserId2;
    private int isDelete;
    private int chatIsPin;
    public boolean checkUserInChat(String userId) {
        System.out.println("user1: "+ chatUserId1 + ", user2: "+chatUserId2+", userId: "+userId);
        return chatUserId1.equals(userId) || chatUserId2.equals(userId);
    }
    public String getAnotherUserId(String userId) {
        if (chatUserId1.equals(userId)) {
            return chatUserId2;
        } else {
            return chatUserId1;
        }
    }
}
