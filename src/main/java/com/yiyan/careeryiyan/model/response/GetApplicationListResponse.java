package com.yiyan.careeryiyan.model.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.yiyan.careeryiyan.model.domain.Apply;
import com.yiyan.careeryiyan.model.domain.User;
import com.yiyan.careeryiyan.model.domain.UserJobPreferences;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class GetApplicationListResponse {
    //apply
    private String id;
    private String userId;
    private String recruitmentId;
    private int status;
    private String cvUrl;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private  LocalDateTime updateTime;

//       "userId": -2147483648,
//               "avatarUrl": "string",
//               "userTag": "string",
//               "education": "string",
//               "userName": "string"
  //  private String userId;
    private String avatarUrl;
    private List<String> userTag;
    private String education;
    private String username;

    public GetApplicationListResponse(Apply apply, User user, List<UserJobPreferences> userJobPreferences) {
        this.id = apply.getId();
        this.userId = apply.getUserId();
        this.recruitmentId = apply.getRecruitmentId();
        this.status = apply.getStatus();
        this.cvUrl = apply.getCvUrl();
        this.createTime = apply.getCreateTime();
        this.updateTime = apply.getUpdateTime();
        this.avatarUrl = user.getAvatarUrl();
        this.userTag = userJobPreferences.stream()
                .map(UserJobPreferences::getRecruitmentTag)
                .collect(Collectors.toList());
        this.education = user.getEducation();
        this.username = user.getUsername();
    }
}
