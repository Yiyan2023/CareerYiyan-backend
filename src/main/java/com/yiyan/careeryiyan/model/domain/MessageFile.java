package com.yiyan.careeryiyan.model.domain;

import lombok.Data;

@Data
public class MessageFile {
    private Integer msgFileId;
    private String msgFileName;
    private Integer msgFileMsgId;
    private String msgFileType;
    private String msgFileExtension;
    private String msgFileUrl;
}
