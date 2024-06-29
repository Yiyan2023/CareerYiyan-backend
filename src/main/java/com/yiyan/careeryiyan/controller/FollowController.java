package com.yiyan.careeryiyan.controller;

import com.yiyan.careeryiyan.exception.BaseException;
import com.yiyan.careeryiyan.model.domain.User;
import com.yiyan.careeryiyan.model.response.StringResponse;
import com.yiyan.careeryiyan.service.FollowService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/follow")
public class FollowController {

    @Resource
    private FollowService followService;

    @PostMapping("/user")
    public ResponseEntity<StringResponse> followUser(@RequestBody Map<String, String> map, HttpServletRequest httpServletRequest) {
        User user = (User) httpServletRequest.getAttribute("user");
        if (user == null)
            throw new BaseException("用户不存在");
        String userId = map.get("userId");
        if(userId.equals(user.getUserId()))
            throw new BaseException("不能关注自己");
        int res = followService.followUser(user.getUserId(), userId);
        String response = res==0 ? "关注成功" : "取消关注成功";
        return ResponseEntity.ok(new StringResponse(response));
    }

    @PostMapping("/enterprise")
    public ResponseEntity<StringResponse> followEnterprise(@RequestBody Map<String, String> map, HttpServletRequest httpServletRequest) {
        User user = (User) httpServletRequest.getAttribute("user");
        if (user == null)
            throw new BaseException("用户不存在");
        String epId = map.get("epId");
        int res = followService.followEnterprise(user.getUserId(), epId);
        String response = res==0 ? "关注企业成功" : "取消关注企业成功";
        return ResponseEntity.ok(new StringResponse(response));
    }

    @GetMapping("/following/users")
    public ResponseEntity<Map<String, Object>> getFollowingUsers(HttpServletRequest httpServletRequest) {
        User user = (User) httpServletRequest.getAttribute("user");
        if (user == null)
            throw new BaseException("用户不存在");
        return ResponseEntity.ok(followService.getFollowingUsers(user.getUserId()));
    }

    @GetMapping("/following/enterprises")
    public ResponseEntity<Map<String, Object>> getFollowingEnterprises(HttpServletRequest httpServletRequest) {
        User user = (User) httpServletRequest.getAttribute("user");
        if (user == null)
            throw new BaseException("用户不存在");

        return ResponseEntity.ok(followService.getFollowingEnterprises(user.getUserId()));
    }

    @GetMapping("/followers")
    public ResponseEntity<Map<String, Object>> getFollowers(
            HttpServletRequest httpServletRequest,
            @RequestParam(required = false) String epId) {
        User user = (User) httpServletRequest.getAttribute("user");
        if (user == null) {
            throw new BaseException("用户不存在");
        }

        Map<String, Object> result;
        if (epId != null && !epId.isEmpty()) {
            // M:验证管理员
            result = followService.getEnterpriseFollowers(epId);
        } else {
            result = followService.getUserFollowers(user.getUserId());
        }
        return ResponseEntity.ok(result);
    }
}