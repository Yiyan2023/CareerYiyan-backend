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
    @Select("select * from enterprise_user  where user_id = #{userId} and is_delete = 0")
    EnterpriseUser getEnterpriseUserByUserId(String userId);

    @Insert("insert into enterprise_user (ep_id, user_id, ep_user_auth,ep_user_createAt)" +
            "values(#{epId}, #{userId}, #{epUserAuth},#{epUserCreateAt})")
    int addEnterpriseUser(EnterpriseUser enterpriseUser);

    @Select("select * " +
            "from user u, enterprise_user eu " +
            "where u.user_id = eu.user_id  and eu.ep_id = #{ep_id} " +
            "and eu.ep_user_createAt = 1 "+
            "and u.is_delete = 0 and eu.is_delete = 0")
    List<EmployeeListResponse> getEmployeeListByEnterpriseId(String enterpriseId);

    @Select("SELECT * from enterprise_user " +
            "where ep_id = #{epId} and ep_user_auth = 0 " +
            "and is_delete = 0")
    EnterpriseUser getEnterpriseAdminByEnterpriseId(String enterpriseId);

    @Insert("insert into enterprise_user (ep_id, user_id, ep_user_auth,ep_user_createAt)" +
            "values(#{epId}, #{userId}, 1,now())")
    int addUserToEnterprise(String userId, String enterpriseId);
}
