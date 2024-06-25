package com.yiyan.careeryiyan.model.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Data

public class Apply {
    private String id;
    private String userId;
    private String recruitmentId;
    private String status;
    private String cvUrl;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private  LocalDateTime createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private  LocalDateTime updateTime;

    public Apply(String userId, String recruitmentId, String status, String cvUrl) {
        this.userId = userId;
        this.recruitmentId = recruitmentId;
        this.status = status;
        this.cvUrl = cvUrl;
        this.createTime = LocalDateTime.now();
        this.updateTime = LocalDateTime.now();
    }


}
