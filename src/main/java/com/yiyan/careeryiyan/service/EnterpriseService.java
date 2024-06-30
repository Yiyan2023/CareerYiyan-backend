package com.yiyan.careeryiyan.service;

import com.yiyan.careeryiyan.mapper.EnterpriseMapper;
import com.yiyan.careeryiyan.mapper.EnterpriseUserMapper;
import com.yiyan.careeryiyan.model.domain.Enterprise;
import com.yiyan.careeryiyan.model.domain.EnterpriseUser;
//import com.yiyan.careeryiyan.model.response.EmployeeListResponse;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

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


    public Enterprise getEnterpriseByEpId(String epId) {
        return enterpriseMapper.getEnterpriseByEpId(epId);
    }
    public List<Map<String, Object>> getEmployeeListByEnterpriseId(String enterpriseId) {
        return enterpriseUserMapper.getEmployeeListByEnterpriseId(enterpriseId);
    }


    public EnterpriseUser getEnterpriseAdminByEnterpriseId(String enterpriseId) {
        return enterpriseUserMapper.getEnterpriseAdminByEnterpriseId(enterpriseId);
    }

    public Enterprise getEnterpriseByName(String enterpriseName) {
        return enterpriseMapper.getEnterpriseByName(enterpriseName);
    }

    public int  addUserToEnterprise(String userId, String epId) {
        return enterpriseUserMapper.addUserToEnterprise(userId, epId);
    }

    public int transferAdmin(String oldEpUserId, String newEpUserId) {
        return enterpriseUserMapper.transferAdmin(oldEpUserId, newEpUserId);
    }
}
