package com.yiyan.careeryiyan.model;

import lombok.Data;

@Data
public class MessageToFrontend<T> {
    private T msg;
    private int type;
    public MessageToFrontend(T msg, int type) {
        this.msg = msg;
        this.type = type;
    }
}
