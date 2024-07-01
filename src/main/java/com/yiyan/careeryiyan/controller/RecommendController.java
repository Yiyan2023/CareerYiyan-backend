package com.yiyan.careeryiyan.controller;

import com.yiyan.careeryiyan.model.domain.User;
import com.yiyan.careeryiyan.service.RecommendService;
import com.yiyan.careeryiyan.service.UserService;
import com.yiyan.careeryiyan.utils.MapUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/recommend")
public class RecommendController {
    @Resource
    private RecommendService recommendService;
    @Resource
    private UserService userService;
    @GetMapping("/getHotRecruitmentList")
    public ResponseEntity getHotRecruitmentList() {
        return ResponseEntity.ok(MapUtil.convertKeysToCamelCase(recommendService.getHotRecruitmentList()));
    }



    @GetMapping("/getHotEnterpriseList")
    public ResponseEntity getHotEnterpriseList() {
        return ResponseEntity.ok(MapUtil.convertKeysToCamelCase(recommendService.getHotEnterpriseList()));
    }

    @GetMapping("/getHotUserList")
    public ResponseEntity getHotUserList() {
        return ResponseEntity.ok(MapUtil.convertKeysToCamelCase(recommendService.getHotUserList()));
    }

    @GetMapping("/getHotPostList")
    public ResponseEntity getHotPostList() {
        return ResponseEntity.ok(MapUtil.convertKeysToCamelCase(recommendService.getHotPostList()));
    }

    //如果用户填写了意向岗位，则依照意向推荐；否则按游客处理，推荐“热门”内容
    @GetMapping("/getRecruitmentList")
    public ResponseEntity getRecruitmentList(HttpServletRequest httpServletRequest) {
        //查看用户意向岗位
        User user = (User) httpServletRequest.getAttribute("user");
        List<String> userRcTags= userService.getUserRcTags(user.getUserId());
        if(userRcTags.size()==0){
            return ResponseEntity.ok(MapUtil.convertKeysToCamelCase(recommendService.getHotRecruitmentList()));
        }
        return ResponseEntity.ok(MapUtil.convertKeysToCamelCase(recommendService.getRecommendRecruitments(userRcTags)));
    }

    @GetMapping("/getUserList")
    public ResponseEntity getUserList(HttpServletRequest httpServletRequest) {
        //查看用户意向岗位
        User user = (User) httpServletRequest.getAttribute("user");
        List<String> userRcTags= userService.getUserRcTags(user.getUserId());
        if(userRcTags.size()==0){
            return ResponseEntity.ok(MapUtil.convertKeysToCamelCase(recommendService.getHotUserList()));
        }
        return ResponseEntity.ok(MapUtil.convertKeysToCamelCase(recommendService.getRecommendUsers(userRcTags)));
    }

    @GetMapping("/getEnterpriseList")
    public ResponseEntity getEnterpriseList(HttpServletRequest httpServletRequest) {
        //查看用户意向岗位
        User user = (User) httpServletRequest.getAttribute("user");
        List<String> userRcTags= userService.getUserRcTags(user.getUserId());
        if(userRcTags.size()==0){
            return ResponseEntity.ok(MapUtil.convertKeysToCamelCase(recommendService.getHotEnterpriseList()));
        }
        return ResponseEntity.ok(MapUtil.convertKeysToCamelCase(recommendService.getRecommendEnterprises(userRcTags)));

    }

}
