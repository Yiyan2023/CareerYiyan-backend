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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime applyCreateAt;
    private String applyCvUrl;
    private String applyId;
    private int applyStatus;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime applyUpdateAt;
    private String isDelete;
    private String rcId;
    private String userId;

    public Apply(String userId, String rcId, int applyStatus, String applyCvUrl) {
        this.userId = userId;
        this.rcId = rcId;
        this.applyStatus = applyStatus;
        this.applyCvUrl = applyCvUrl;
        this.applyCreateAt = LocalDateTime.now();
        this.applyUpdateAt = this.applyCreateAt;

    }
}
