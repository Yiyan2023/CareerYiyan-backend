package com.yiyan.careeryiyan.controller;


import com.yiyan.careeryiyan.model.request.SearchEnterpriseRequest;
import com.yiyan.careeryiyan.model.request.SearchRecruitmentRequest;
import com.yiyan.careeryiyan.model.request.SearchUserRequest;
import com.yiyan.careeryiyan.service.SearchService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/search")
public class SearchController {
    @Resource
    SearchService searchService;

    @PostMapping("/recruitment")
    public ResponseEntity searchRecruitment(@RequestBody SearchRecruitmentRequest searchRecruitmentRequest,
                                                            HttpServletRequest httpServletRequest) {
        return ResponseEntity.ok(searchService.searchRecruitment(searchRecruitmentRequest));

    }

    @PostMapping("/enterprise")
    public ResponseEntity searchEnterprise(@RequestBody SearchEnterpriseRequest searchEnterpriseRequest,
                                                            HttpServletRequest httpServletRequest) {
        return ResponseEntity.ok(searchService.searchEnterprise(searchEnterpriseRequest));
    }

    @PostMapping("/user")
    public ResponseEntity searchUser(@RequestBody SearchUserRequest searchUserRequest,
                                                            HttpServletRequest httpServletRequest) {
        return ResponseEntity.ok(searchService.searchUser(searchUserRequest));
    }
}
