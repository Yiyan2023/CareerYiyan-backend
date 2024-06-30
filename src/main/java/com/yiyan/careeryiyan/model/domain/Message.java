package com.yiyan.careeryiyan.model.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;

import java.time.LocalDateTime;


@Data
public class Message {
    private String msgId;
    private String msgContent;
    private String msgChatId;
    private String msgCreateDate;
    private String msgCreateTime;
    private int msgIsRead;
    private String msgSendUserId;
    private String msgReplyMsgId;
    private int msgIsSystem;
    private int isDelete;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime msgCreateAt;

}
