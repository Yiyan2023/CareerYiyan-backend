package com.yiyan.careeryiyan.mapper;

import com.yiyan.careeryiyan.model.domain.Enterprise;
import com.yiyan.careeryiyan.model.request.EditEnterpriseRequest;
import org.apache.ibatis.annotations.*;

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

    @Update("update enterprise set ep_avatar_url=#{epAvatarUrl} where ep_id=#{epId};")
    int updateAvatar(String epId, String epAvatarUrl);

    @Update("update enterprise set ep_name=#{epName}, ep_addr=#{epAddr}, " +
            "ep_desc=#{epDesc}, ep_type=#{epType} where ep_id=#{epId};")
    int editEnterprise(EditEnterpriseRequest editEnterpriseRequest);
}
