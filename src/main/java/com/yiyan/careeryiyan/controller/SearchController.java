package com.yiyan.careeryiyan.controller;


import com.yiyan.careeryiyan.model.domain.Enterprise;
import com.yiyan.careeryiyan.model.request.SearchEnterpriseRequest;
import com.yiyan.careeryiyan.model.request.SearchRecruitmentRequest;
import com.yiyan.careeryiyan.model.request.SearchUserRequest;
import com.yiyan.careeryiyan.model.response.RecruitmentDetailResponse;
import com.yiyan.careeryiyan.model.response.StringResponse;
import com.yiyan.careeryiyan.model.response.UserDetailResponse;
import com.yiyan.careeryiyan.service.SearchService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/search")
public class SearchController {
    @Resource
    SearchService searchService;

    @PostMapping("/recruitment")
    public ResponseEntity searchRecruitment(@RequestBody SearchRecruitmentRequest searchRecruitmentRequest,
                                                            HttpServletRequest httpServletRequest) {
        List<RecruitmentDetailResponse> recruitmentDetailResponses = searchService.searchRecruitment(searchRecruitmentRequest);
        System.out.println(searchRecruitmentRequest.getRecruitmentName()+searchRecruitmentRequest.getPageNum()+searchRecruitmentRequest.getPageSize());
        return ResponseEntity.ok(recruitmentDetailResponses);

    }

    @PostMapping("/enterprise")
    public ResponseEntity searchEnterprise(@RequestBody SearchEnterpriseRequest searchEnterpriseRequest,
                                                            HttpServletRequest httpServletRequest) {
        List<Enterprise> enterpriseDetailResponses = searchService.searchEnterprise(searchEnterpriseRequest);
        return ResponseEntity.ok(enterpriseDetailResponses);
    }

    @PostMapping("/user")
    public ResponseEntity searchUser(@RequestBody SearchUserRequest searchUserRequest,
                                                            HttpServletRequest httpServletRequest) {
        List<UserDetailResponse> userDetailResponses = searchService.searchUser(searchUserRequest);
        return ResponseEntity.ok(userDetailResponses);
    }
}
