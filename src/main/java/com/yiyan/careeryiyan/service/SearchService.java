package com.yiyan.careeryiyan.service;


import com.yiyan.careeryiyan.mapper.SearchMapper;
import com.yiyan.careeryiyan.model.domain.Enterprise;
import com.yiyan.careeryiyan.model.request.SearchEnterpriseRequest;
import com.yiyan.careeryiyan.model.request.SearchRecruitmentRequest;
import com.yiyan.careeryiyan.model.request.SearchUserRequest;
//import com.yiyan.careeryiyan.model.response.RecruitmentDetailResponse;
import com.yiyan.careeryiyan.model.response.UserDetailResponse;
import io.jsonwebtoken.impl.crypto.MacProvider;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class SearchService {
    @Resource
    SearchMapper searchMapper;

    public Map searchRecruitment(SearchRecruitmentRequest searchRecruitmentRequest) {

        return Map.of("total", searchMapper.getSearchRecruitmentTotal(searchRecruitmentRequest), "list", searchMapper.searchRecruitment(searchRecruitmentRequest));
    }


    public Map searchEnterprise(SearchEnterpriseRequest searchEnterpriseRequest) {
        return Map.of("total", searchMapper.getSearchEnterpriseTotal(searchEnterpriseRequest), "list", searchMapper.searchEnterprise(searchEnterpriseRequest));
    }

    public Map searchUser(SearchUserRequest searchUserRequest) {
        int total = searchMapper.getSearchUserTotal(searchUserRequest);
        return Map.of("total", total, "list", searchMapper.searchUser(searchUserRequest));
    }
}
