package com.yiyan.careeryiyan.controller;

import com.yiyan.careeryiyan.exception.BaseException;
import com.yiyan.careeryiyan.model.domain.Enterprise;
import com.yiyan.careeryiyan.model.domain.EnterpriseUser;
import com.yiyan.careeryiyan.model.domain.User;
import com.yiyan.careeryiyan.model.request.AddEnterpriseRequest;
import com.yiyan.careeryiyan.model.request.GetEnterpriseInfoRequest;
import com.yiyan.careeryiyan.service.EnterpriseService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.ibatis.annotations.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/enterprise")
public class EnterpriseController {
    @Resource
    private EnterpriseService enterpriseService;

    @PostMapping("/addEnterprise")
    public ResponseEntity addEnterprise(@RequestBody AddEnterpriseRequest addEnterpriseRequest, HttpServletRequest request) {
        User user = (User) request.getAttribute("user");
        EnterpriseUser enterpriseUser = enterpriseService.getEnterpriseUserById(user.getId());
        if (enterpriseUser != null) {
           throw new BaseException("用户已创建过企业或已加入企业！");
        }
        Enterprise enterprise = new Enterprise();
        enterprise.setEnterpriseAddress(addEnterpriseRequest.getEnterpriseAddress());
        enterprise.setEnterpriseName(addEnterpriseRequest.getEnterpriseName());
        enterprise.setEnterpriseDescription(addEnterpriseRequest.getEnterpriseDescription());
        enterprise.setEnterpriseLicense(addEnterpriseRequest.getEnterpriseLicense());
        enterprise.setEnterpriseType(addEnterpriseRequest.getEnterpriseType());
        enterprise.setAvatarUrl(addEnterpriseRequest.getAvatarUrl());
        enterprise.setCreateTime(LocalDateTime.now());
        int id = enterpriseService.addEnterprise(enterprise);
        if (id > 0) {
            enterprise.setId(String.valueOf(id));
            enterpriseUser = new EnterpriseUser();
            enterpriseUser.setUserId(user.getId());
            enterpriseUser.setEnterpriseId(enterprise.getId());
            enterpriseUser.setRole(0);
            enterpriseUser.setCreateTime(LocalDateTime.now());
            if (enterpriseService.addEnterpriseUser(enterpriseUser)<=0){
               throw new BaseException("创建企业失败！");
            }
            return ResponseEntity.ok("add enterprise success");
        }
        throw new BaseException("创建企业失败！");
    }

    @PostMapping("/getInfo")
    public ResponseEntity getInfo(@RequestBody GetEnterpriseInfoRequest rq){

        Enterprise enterprise = enterpriseService.getEnterpriseById(rq.getEnterpriseId());
        //System.out.println(enterpriseId);
        if (enterprise == null){
            throw new BaseException("企业不存在");
        }
        return ResponseEntity.ok(enterprise);
    }
}
