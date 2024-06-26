package com.yiyan.careeryiyan.service;

import com.yiyan.careeryiyan.mapper.ApplyMapper;
import com.yiyan.careeryiyan.mapper.RecruitmentMapper;
import com.yiyan.careeryiyan.model.domain.Apply;
import com.yiyan.careeryiyan.model.domain.Recruitment;
import com.yiyan.careeryiyan.model.request.AddApplyRequest;
import com.yiyan.careeryiyan.model.request.AddRecruitmentRequest;
import com.yiyan.careeryiyan.model.request.EditRecruitmentRequest;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecruitmentService {
    @Resource
    RecruitmentMapper recruitmentMapper;
    @Resource
    ApplyMapper applyMapper;
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

    public int addApply(Apply apply) {
        return applyMapper.addApply(apply);
    }

    public Apply getApplyByUserIdAndRecruitmentId(String userId, String recruitmentId) {
        return applyMapper.getApplyByUserIdAndRecruitmentId(userId, recruitmentId);
    }

    public List<Apply> getApplyByRecruitmentId(String recruitmentId) {
        return applyMapper.getApplyByRecruitmentId(recruitmentId);
    }
}
