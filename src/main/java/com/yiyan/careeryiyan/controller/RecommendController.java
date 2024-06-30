package com.yiyan.careeryiyan.controller;

import com.yiyan.careeryiyan.service.RecommendService;
import com.yiyan.careeryiyan.utils.MapUtil;
import jakarta.annotation.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.util.Map;

@RestController
@RequestMapping("/recommend")
public class RecommendController {
    @Resource
    private RecommendService recommendService;
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

}
