package com.yiyan.careeryiyan.controller;

import com.yiyan.careeryiyan.model.domain.User;
import com.yiyan.careeryiyan.model.response.StringResponse;
import com.yiyan.careeryiyan.service.NoticeService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/notices")
public class NoticeController {
    @Resource
    private NoticeService noticeService;

    @PostMapping("/all")
    public ResponseEntity getAllNotices(HttpServletRequest request) {
        User user = (User) request.getAttribute("user");
        return ResponseEntity.ok(Map.of("notices", noticeService.getNotices(user.getUserId())));
    }

    @PostMapping("/read")
    public ResponseEntity markAsRead(@RequestBody Map<String, Object> body, HttpServletRequest request) {
        User user = (User) request.getAttribute("user");
        noticeService.markAsRead(user.getUserId(),(List<String>) body.get("noticeIds"));
        return ResponseEntity.ok(new StringResponse("设为已读成功"));
    }

}
