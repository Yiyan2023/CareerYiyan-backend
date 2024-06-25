package com.yiyan.careeryiyan.mapper;

import com.yiyan.careeryiyan.model.domain.EnterpriseUser;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface EnterpriseUserMapper {
    @Select("select * from EnterpriseUser eu where eu.userId = #{userId} and eu.isDeleted = 0")
    EnterpriseUser getEnterpriseUserByUserId(String userId);

    @Insert("insert into EnterpriseUser (enterpriseId, userId, role,createTime)" +
            "values(#{enterpriseId}, #{userId}, #{role},#{createTime})")
    int addEnterpriseUser(EnterpriseUser enterpriseUser);
}