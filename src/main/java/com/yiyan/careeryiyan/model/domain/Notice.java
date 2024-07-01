package com.yiyan.careeryiyan.model.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Notice {
    private String avatarUrl;
    private int userId;
    private LocalDateTime noticeCreateAt;
    private int epId;
    @JsonProperty("isRead")
    private boolean isRead;
    private String noticeContent;
    private int noticeId;
    private int noticeType;
    private int postId;
}
