package com.yiyan.careeryiyan.mapper;

import com.yiyan.careeryiyan.model.domain.Enterprise;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface EnterpriseMapper {
    @Select(" SELECT * from enterprise where enterprise.id = #{id}")
    Enterprise getEnterpriseById(Integer id);

    @Insert("INSERT into Enterprise(enterpriseName, enterpriseAddress, enterpriseDescription, enterpriseType, enterpriseType, enterpriseLicense,createTime,avatarUrl) " +
            "values(#{enterpriseName}, #{enterpriseAddress}, #{enterpriseDescription}, #{enterpriseType}, #{enterpriseType}, #{enterpriseLicense}, #{createTime}, #{avatarUrl})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int addEnterprise(Enterprise enterprise);
}
