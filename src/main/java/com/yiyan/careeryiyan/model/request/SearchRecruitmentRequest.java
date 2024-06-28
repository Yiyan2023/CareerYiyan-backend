package com.yiyan.careeryiyan.model.request;


import lombok.Data;

@Data
public class SearchRecruitmentRequest {
    private String rcName;
    private int pageSize;
    private int pageNum;

    public int getOffset() {
        return (pageNum-1) * pageSize;
    }
}
