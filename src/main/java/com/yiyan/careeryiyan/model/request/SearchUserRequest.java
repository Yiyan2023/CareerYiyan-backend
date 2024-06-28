package com.yiyan.careeryiyan.model.request;

import lombok.Data;

@Data
public class SearchUserRequest {
    private String userNickname;
    private int pageSize;
    private int pageNum;
    public int getOffset() {
        return (pageNum-1) * pageSize;
    }
}
