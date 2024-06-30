package com.yiyan.careeryiyan.model.request;

import lombok.Data;

@Data
public class FetchMessagesRequest {
    private String chatId;
    private int msgPage;
    private int numMsgInPage;
}
