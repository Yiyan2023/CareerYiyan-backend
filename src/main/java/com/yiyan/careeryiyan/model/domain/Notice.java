package com.yiyan.careeryiyan.model.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Notice {
    private String avatarUrl;
    private String userId;
    private LocalDateTime noticeCreateAt;
    private String epId;
    @JsonProperty("isRead")
    private boolean isRead;
    private String noticeContent;
    private String noticeId;
    private String noticeType;
    private String postId;
}
