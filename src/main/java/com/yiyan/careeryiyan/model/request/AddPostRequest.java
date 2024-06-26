package com.yiyan.careeryiyan.model.request;

import com.yiyan.careeryiyan.model.domain.Like;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class AddPostRequest {
    private String content;
    private String title;
    List<MultipartFile> photos;
}
