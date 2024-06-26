package com.yiyan.careeryiyan.model.request;


import lombok.Data;

@Data
public class SearchRecruitmentRequest {
    private String recruitmentName;
    private int pageSize;
    private int pageNum;

    public int getOffset() {
        return pageNum * pageSize;
    }
}
