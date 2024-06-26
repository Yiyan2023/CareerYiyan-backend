package com.yiyan.careeryiyan.service;

import com.yiyan.careeryiyan.mapper.EnterpriseMapper;
import com.yiyan.careeryiyan.mapper.EnterpriseUserMapper;
import com.yiyan.careeryiyan.model.domain.Enterprise;
import com.yiyan.careeryiyan.model.domain.EnterpriseUser;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class EnterpriseService {
    @Resource
    private EnterpriseUserMapper enterpriseUserMapper;
    @Resource
    private EnterpriseMapper enterpriseMapper;


    public EnterpriseUser getEnterpriseUserByUserId(String userId) {
        return enterpriseUserMapper.getEnterpriseUserByUserId(userId);
    }

    public int addEnterprise(Enterprise enterprise) {
        return enterpriseMapper.addEnterprise(enterprise);
    }

    public int addEnterpriseUser(EnterpriseUser enterpriseUser) {
        return enterpriseUserMapper.addEnterpriseUser(enterpriseUser);
    }


    public Enterprise getEnterpriseById(String id) {
        return enterpriseMapper.getEnterpriseById(id);
    }


}
