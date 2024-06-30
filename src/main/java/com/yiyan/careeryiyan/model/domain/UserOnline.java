package com.yiyan.careeryiyan.model.domain;

import lombok.Data;

@Data
public class UserOnline {
    private String userOnlineId;
    private String userOnlineUserId;
    private String userOnlineStatus;
    private String userOnlineLastChangeAt;
}
