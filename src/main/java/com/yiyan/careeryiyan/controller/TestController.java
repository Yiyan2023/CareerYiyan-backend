package com.yiyan.careeryiyan.controller;

import com.yiyan.careeryiyan.mapper.EnterpriseMapper;
import com.yiyan.careeryiyan.model.domain.Enterprise;
import jakarta.annotation.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController()
@RequestMapping("/ttt")
public class TestController {
    @Resource
    EnterpriseMapper enterpriseMapper;
    @PostMapping("/addEnterprise")
    public ResponseEntity addEnterprise(@RequestBody Enterprise enterprise) {
        int epId=enterpriseMapper.addEnterprise(enterprise);
        return ResponseEntity.ok(Map.of("epId", epId));
    }
}
