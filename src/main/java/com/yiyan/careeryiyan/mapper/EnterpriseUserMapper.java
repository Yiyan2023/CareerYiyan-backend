package com.yiyan.careeryiyan.mapper;

import com.yiyan.careeryiyan.model.domain.EnterpriseUser;
import com.yiyan.careeryiyan.model.response.EmployeeListResponse;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface EnterpriseUserMapper {
    @Select("select * from EnterpriseUser eu where eu.userId = #{userId} and eu.isDeleted = 0")
    EnterpriseUser getEnterpriseUserByUserId(String userId);

    @Insert("insert into EnterpriseUser (enterpriseId, userId, role,createTime)" +
            "values(#{enterpriseId}, #{userId}, #{role},#{createTime})")
    int addEnterpriseUser(EnterpriseUser enterpriseUser);

    @Select("select * " +
            "from User u, EnterpriseUser eu " +
            "where u.id = eu.userId and eu.isDeleted = 0 and eu.enterpriseId = #{enterpriseId} " +
            "and eu.role = 1")
    List<EmployeeListResponse> getEmployeeListByEnterpriseId(String enterpriseId);

    @Select("SELECT * from EnterpriseUser eu " +
            "where eu.enterpriseId = #{enterpriseId} and eu.isDeleted = 0 and eu.role = 0")
    EnterpriseUser getEnterpriseAdminByEnterpriseId(String enterpriseId);
}
