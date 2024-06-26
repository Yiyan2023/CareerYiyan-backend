package com.yiyan.careeryiyan.model.request;

import lombok.Data;

@Data
public class SearchUserRequest {
    private String userName;
    private int pageSize;
    private int pageNum;
    public int getOffset() {
        return pageNum * pageSize;
    }
}
