package com.yiyan.careeryiyan.service;

import com.yiyan.careeryiyan.exception.BaseException;
import com.yiyan.careeryiyan.mapper.ApplyMapper;
import com.yiyan.careeryiyan.mapper.RecruitmentMapper;
import com.yiyan.careeryiyan.model.domain.Apply;
import com.yiyan.careeryiyan.model.domain.Recruitment;
import com.yiyan.careeryiyan.model.request.AddApplyRequest;
import com.yiyan.careeryiyan.model.request.AddRecruitmentRequest;
import com.yiyan.careeryiyan.model.request.EditRecruitmentRequest;
import com.yiyan.careeryiyan.model.response.UserApplyDetailResponse;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RecruitmentService {
    @Resource
    RecruitmentMapper recruitmentMapper;
    @Resource
    ApplyMapper applyMapper;

    public int addRecruitment(AddRecruitmentRequest addRecruitmentRequest) {
        recruitmentMapper.addRecruitment(addRecruitmentRequest);
        return addRecruitmentRequest.getId();
    }

    public List<Recruitment> getRecruitmentList(String enterpriseId) {
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

    public List<UserApplyDetailResponse> getUserApplyList(String id) {
        return recruitmentMapper.getUserApplyList(id);
    }

    @Transactional(rollbackFor = Exception.class)
    public int changeState(String applyId, int status) {
        if (status == 1 || status == 4) {
            //发offer
            try {
                // 获取加锁的Apply记录
                applyMapper.getApplyAndRecruitmentForUpdate(applyId);
                int rowsAffected = 0;
                // 执行更新操作
                if (status == 1) {
                    rowsAffected = applyMapper.increaseOfferCountAndChangeStatus(applyId, status);
                } else {
                    rowsAffected = applyMapper.decreaseOfferCountAndChangeStatus(applyId, status);
                }
                // 检查更新是否成功
                if (rowsAffected != 2) {
                    System.out.println("rowsAffected="+rowsAffected);
                    //事务回滚
                    throw new IllegalStateException("Failed to update apply status");
                }
            } catch (Exception e) {
                // 记录异常日志
                System.out.println(e);
                throw e;
            }
        }
        return applyMapper.changeStatus(applyId, status);
    }

    public Apply getApplyById(String applyId) {
        return recruitmentMapper.getApplyById(applyId);
    }


}
