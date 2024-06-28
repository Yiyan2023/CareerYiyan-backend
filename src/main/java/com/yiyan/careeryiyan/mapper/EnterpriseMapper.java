package com.yiyan.careeryiyan.mapper;

import com.yiyan.careeryiyan.model.domain.Enterprise;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface EnterpriseMapper {
    @Select(" select * from enterprise\n" +
            " where is_delete=0 and ep_id=#{epId};")
    Enterprise getEnterpriseByEpId(String epId);

    @Insert("insert into enterprise(ep_name, ep_addr, ep_desc, ep_type, ep_license, ep_create_at, ep_avatar_url) " +
            "values(#{epName}, #{epAddr}, #{epDesc}, #{epType}, #{epLicense}, #{epCreateAt}, #{epAvatarUrl});")
//@Insert("insert into enterprise(epName, epAddr, epDesc, epType, epLicense, epCreateAt, epAvatarUrl) " +
//        "values(#{epName}, #{epAddr}, #{epDesc}, #{epType}, #{epLicense}, #{epCreateAt}, #{epAvatarUrl});")
    @Options(useGeneratedKeys = true, keyProperty = "epId")
    int addEnterprise(Enterprise enterprise);


    @Select("SELECT * from enterprise where ep_name = #{epName}")
    Enterprise getEnterpriseByName(String enterpriseName);
}
