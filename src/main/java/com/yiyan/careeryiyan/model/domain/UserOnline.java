package com.yiyan.careeryiyan.model.domain;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserOnline {
    private String userOnlineId;
    private String userOnlineUserId;
    private String userOnlineStatus;
    private LocalDateTime userOnlineLastChangeAt;
    private String userOnlineChatId;
}
