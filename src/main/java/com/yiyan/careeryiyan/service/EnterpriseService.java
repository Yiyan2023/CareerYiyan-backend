package com.yiyan.careeryiyan.service;

import com.yiyan.careeryiyan.mapper.EnterpriseMapper;
import com.yiyan.careeryiyan.mapper.EnterpriseUserMapper;
import com.yiyan.careeryiyan.mapper.UserMapper;
import com.yiyan.careeryiyan.model.domain.Enterprise;
import com.yiyan.careeryiyan.model.domain.EnterpriseUser;
import com.yiyan.careeryiyan.model.domain.User;
import com.yiyan.careeryiyan.model.response.EmployeeListResponse;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public List<EmployeeListResponse> getEmployeeListByEnterpriseId(String enterpriseId) {
        return enterpriseUserMapper.getEmployeeListByEnterpriseId(enterpriseId);
    }


    public EnterpriseUser getEnterpriseAdminByEnterpriseId(String enterpriseId) {
        return enterpriseUserMapper.getEnterpriseAdminByEnterpriseId(enterpriseId);
    }
}
