package com.yiyan.careeryiyan.model.request;

import com.yiyan.careeryiyan.model.domain.Message;
import com.yiyan.careeryiyan.model.domain.MessageFile;
import lombok.Data;

import java.util.List;

@Data
public class SendRequest {
    private Message message;
    private List<MessageFile> files;
}
