package com.yiyan.careeryiyan.model.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Data
public class Enterprise {
    private String epAddr;
    private String epAvatarUrl;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime epCreateAt;
    private String epDesc;
    private String epId;
    /**
     * 企业凭证的链接，目前没用
     */
    private String epLicense;
    private String epName;
    /**
     * 企业类型，比如：互联网企业、国有企业
     */
    private String epType;
    private int isDelete;
}