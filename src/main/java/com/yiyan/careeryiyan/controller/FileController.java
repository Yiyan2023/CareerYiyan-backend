package com.yiyan.careeryiyan.controller;


import com.yiyan.careeryiyan.config.OSSConfig;
import com.yiyan.careeryiyan.exception.BaseException;
import com.yiyan.careeryiyan.model.domain.User;
import com.yiyan.careeryiyan.model.response.StringResponse;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Random;

@RestController
@RequestMapping("/file")
public class FileController {
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    public static String generateRandomString(int length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(index));
        }

        return sb.toString();
    }
    @Resource
    private OSSConfig ossConfig;
    @PostMapping("/upload")
    public ResponseEntity<StringResponse> upload(@RequestParam("file") MultipartFile file,
                                                   HttpServletRequest httpServletRequest) throws IOException {
        User user = (User) httpServletRequest.getAttribute("user");
        String name = user.getNickname() + generateRandomString(10);

        String res = ossConfig.upload(file, "other", name);
        if (res != null){
            return ResponseEntity.ok(new StringResponse(res));
        } else {
            throw new BaseException("上传失败");
        }
    }
}
