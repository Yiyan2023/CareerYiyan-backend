package com.yiyan.careeryiyan.service;


import com.yiyan.careeryiyan.mapper.SearchMapper;
import com.yiyan.careeryiyan.model.domain.Enterprise;
import com.yiyan.careeryiyan.model.request.SearchEnterpriseRequest;
import com.yiyan.careeryiyan.model.request.SearchRecruitmentRequest;
import com.yiyan.careeryiyan.model.request.SearchUserRequest;
import com.yiyan.careeryiyan.model.response.RecruitmentDetailResponse;
import com.yiyan.careeryiyan.model.response.UserDetailResponse;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchService {
    @Resource
    SearchMapper searchMapper;

    public List<RecruitmentDetailResponse> searchRecruitment(SearchRecruitmentRequest searchRecruitmentRequest) {

        return searchMapper.searchRecruitment(searchRecruitmentRequest);
    }



    public List<Enterprise> searchEnterprise(SearchEnterpriseRequest searchEnterpriseRequest) {
        return searchMapper.searchEnterprise(searchEnterpriseRequest);
    }

    public List<UserDetailResponse> searchUser(SearchUserRequest searchUserRequest) {
        return searchMapper.searchUser(searchUserRequest);
    }
}
