package com.yiyan.careeryiyan.service;

import com.yiyan.careeryiyan.mapper.RecruitmentMapper;
import com.yiyan.careeryiyan.model.request.AddRecruitmentRequest;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class RecruitmentService {
    @Resource
    RecruitmentMapper recruitmentMapper;
    public int addRecruitment(AddRecruitmentRequest addRecruitmentRequest){
        return recruitmentMapper.addRecruitment(addRecruitmentRequest);
    }
}
