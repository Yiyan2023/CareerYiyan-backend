package com.yiyan.careeryiyan.model.request;

import com.yiyan.careeryiyan.model.domain.User;
import lombok.Data;

import java.util.List;

@Data
public class ModifyInfoRequest {
    private User user;
    private List<String> rcTag;
}
