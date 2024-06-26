package com.yiyan.careeryiyan.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
@Data
public class AddCommentRequest {
    @NotBlank(message = "Post ID cannot be blank")
    private String postId;

    @NotBlank(message = "Content cannot be blank")
    private String content;

    private String parentId = "0";  // 设置默认值
}
