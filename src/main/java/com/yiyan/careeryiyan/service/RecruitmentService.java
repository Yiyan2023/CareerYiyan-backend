package com.yiyan.careeryiyan.service;

import com.yiyan.careeryiyan.mapper.RecruitmentMapper;
import com.yiyan.careeryiyan.model.domain.Recruitment;
import com.yiyan.careeryiyan.model.request.AddRecruitmentRequest;
import com.yiyan.careeryiyan.model.request.EditRecruitmentRequest;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecruitmentService {
    @Resource
    RecruitmentMapper recruitmentMapper;
    public int addRecruitment(AddRecruitmentRequest addRecruitmentRequest){
        return recruitmentMapper.addRecruitment(addRecruitmentRequest);
    }

    public List<Recruitment> getRecruitmentList(String enterpriseId){
        return recruitmentMapper.getRecruitmentList(enterpriseId);
    }


    public int updateRecruitment(EditRecruitmentRequest editRecruitmentRequest) {
        return recruitmentMapper.updateRecruitment(editRecruitmentRequest);
    }

    public Recruitment getRecruitmentById(String id) {
        return recruitmentMapper.getRecruitmentById(id);
    }

    public int deleteRecruitment(String id) {
        return recruitmentMapper.deleteRecruitment(id);
    }
}
