package com.yiyan.careeryiyan.controller;

import com.yiyan.careeryiyan.exception.BaseException;
import com.yiyan.careeryiyan.mapper.EnterpriseUserMapper;
import com.yiyan.careeryiyan.mapper.FollowMapper;
import com.yiyan.careeryiyan.model.domain.*;
import com.yiyan.careeryiyan.model.response.StringResponse;
import com.yiyan.careeryiyan.service.FollowService;
import com.yiyan.careeryiyan.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.sound.midi.Track;
import java.util.Map;

@RestController
@RequestMapping("/follow")
public class FollowController {

    @Resource
    private FollowService followService;

    @Resource
    EnterpriseUserMapper enterpriseUserMapper;
    @Resource
    UserService userService;
    @Resource
    private FollowMapper followMapper;

    @PostMapping("/user")
    public ResponseEntity<StringResponse> followUser(@RequestBody Map<String, String> map, HttpServletRequest httpServletRequest) {
        User user = (User) httpServletRequest.getAttribute("user");
        String targetId = map.get("userId");
        FollowUser followUser = followMapper.getFollowUser(user.getUserId(), targetId);
        if (followUser != null && !followUser.getIsDelete()){
            return ResponseEntity.ok(new StringResponse("已关注"));
        }else{
            int res = 0;
            if (followUser!=null){
                res =followMapper.updateFollowUser(user.getUserId(), targetId, false);
            }else{
                res = followMapper.insertFollowUser(user.getUserId(), targetId);
            }
            if (res >0){
                userService.updateInfluence(5, targetId);
                return ResponseEntity.ok(new StringResponse("关注成功"));
            }
        }

        throw new BaseException("关注失败");
    }

    @PostMapping("/undo")
    public ResponseEntity<StringResponse> undoFollow(@RequestBody Map<String, String> map, HttpServletRequest httpServletRequest) {
        User user = (User) httpServletRequest.getAttribute("user");
        String targetId = map.get("userId");
        boolean isFollow = followService.checkFollow(user.getUserId(),targetId);
        if (!isFollow) {
            return ResponseEntity.ok(new StringResponse("你还未关注该用户"));
        }else{
            int res = followMapper.updateFollowUser(user.getUserId(), targetId, true);
            if (res >0){
                userService.updateInfluence(-5, targetId);
                return ResponseEntity.ok(new StringResponse("取消关注成功"));
            }
        }
        throw new BaseException("取消关注失败");
    }

    @PostMapping("/enterprise")
    public ResponseEntity<StringResponse> followEnterprise(@RequestBody Map<String, String> map, HttpServletRequest httpServletRequest) {
        User user = (User) httpServletRequest.getAttribute("user");
        String epId = map.get("epId");
        FollowEnterprise followEnterprise = followMapper.getFollowEnterprise(user.getUserId(), epId);
        if (followEnterprise != null && !followEnterprise.getIsDelete()) {
            return ResponseEntity.ok(new StringResponse("你已关注过该企业"));
        }
       int res = 0;
        if (followEnterprise != null) {
            res = followMapper.updateFollowEnterprise(user.getUserId(), epId, false);
        } else {
            res = followMapper.insertFollowEnterprise(user.getUserId(), epId);
        }
        if (res > 0) {
            return ResponseEntity.ok(new StringResponse("关注成功"));
        }
        throw new BaseException("关注失败");
    }

    @PostMapping("/undoEp")
    public ResponseEntity<StringResponse> undoFollowEnterprise(@RequestBody Map<String, String> map, HttpServletRequest httpServletRequest) {
        User user = (User) httpServletRequest.getAttribute("user");
        String epId = map.get("epId");
        boolean isFollow = followService.checkFollowEnterprise(user.getUserId(),epId);
        if (!isFollow) {
            return ResponseEntity.ok(new StringResponse("你还未关注该企业"));
        }else{
            int res = followMapper.updateFollowEnterprise(user.getUserId(), epId, true);
            if (res >0){
                return ResponseEntity.ok(new StringResponse("取消关注成功"));
            }
        }
        throw new BaseException("取消关注失败");
    }

    @PostMapping("/checkEp")
    public ResponseEntity<Map<String, Object>> checkFollowEnterprise(@RequestBody Map<String, String> map, HttpServletRequest httpServletRequest) {
        User user = (User) httpServletRequest.getAttribute("user");
        if (user == null)
            throw new BaseException("用户不存在");
        String epId = map.get("epId");

        return ResponseEntity.ok(Map.of("isFollow",followService.checkFollowEnterprise(user.getUserId(), epId)));
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
            EnterpriseUser eu=enterpriseUserMapper.getEnterpriseAdminByEnterpriseId(epId);
            if (eu == null) {
                throw new BaseException("企业不存在");
            }
            if(!eu.getUserId().equals(user.getUserId())){
                throw new BaseException("非管理员不能查看企业粉丝列表");
            }
            result = followService.getEnterpriseFollowers(epId);
        } else {
            result = followService.getUserFollowers(user.getUserId());
        }
        return ResponseEntity.ok(result);
    }

    //检查是否关注用户
    @PostMapping("/check")
    public ResponseEntity<Map<String, Object>> checkFollow(@RequestBody Map<String, String> map, HttpServletRequest httpServletRequest) {
        User user = (User) httpServletRequest.getAttribute("user");
        if (user == null){
            throw new BaseException("用户不存在");
        }
        String targetUserId = map.get("userId");

        return ResponseEntity.ok(Map.of("isFollow",followService.checkFollow(user.getUserId(), targetUserId)));
    }


}