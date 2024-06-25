package com.yiyan.careeryiyan.controller;

import com.yiyan.careeryiyan.model.domain.Enterprise;
import com.yiyan.careeryiyan.model.domain.EnterpriseUser;
import com.yiyan.careeryiyan.model.domain.User;
import com.yiyan.careeryiyan.model.request.AddEnterpriseRequest;
import com.yiyan.careeryiyan.service.EnterpriseService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
            return new ResponseEntity<>(ResponseEntity.status(HttpStatus.CONFLICT).build(), HttpStatus.CONFLICT);
        }
        Enterprise enterprise = new Enterprise();
        enterprise.setEnterpriseAddress(addEnterpriseRequest.getEnterpriseAddress());
        enterprise.setEnterpriseName(addEnterpriseRequest.getEnterpriseName());
        enterprise.setEnterpriseDescription(addEnterpriseRequest.getEnterpriseDescription());
        enterprise.setEnterpriseLicense(addEnterpriseRequest.getEnterpriseLicense());
        enterprise.setEnterpriseType(addEnterpriseRequest.getEnterpriseType());
        enterprise.setAvatarUrl(addEnterpriseRequest.getAvatarUrl());
        int id = enterpriseService.addEnterprise(enterprise);
        if (id > 0) {
            enterprise.setId(String.valueOf(id));
            return ResponseEntity.ok("add enterprise success");
        }
        return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
    }
}
