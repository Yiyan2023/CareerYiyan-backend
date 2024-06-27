package com.yiyan.careeryiyan.mapper;

import com.yiyan.careeryiyan.model.domain.Enterprise;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface EnterpriseMapper {
    @Select(" SELECT * from Enterprise where Enterprise.id = #{id}")
    Enterprise getEnterpriseById(String id);

    @Insert("INSERT into Enterprise(enterpriseName, enterpriseAddress, enterpriseDescription, enterpriseType, enterpriseLicense,createTime,avatarUrl) " +
            "values(#{enterpriseName}, #{enterpriseAddress}, #{enterpriseDescription}, #{enterpriseType}, #{enterpriseLicense}, #{createTime}, #{avatarUrl})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int addEnterprise(Enterprise enterprise);


    @Select("SELECT * from Enterprise where enterpriseName = #{enterpriseName}")
    Enterprise getEnterpriseByName(String enterpriseName);
}
