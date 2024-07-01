package com.yiyan.careeryiyan.mapper;

import com.yiyan.careeryiyan.model.domain.EnterpriseUser;
//import com.yiyan.careeryiyan.model.response.EmployeeListResponse;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

@Mapper
public interface EnterpriseUserMapper {
    @Select("select * from enterprise_user  where user_id = #{userId} and is_delete = 0")
    EnterpriseUser getEnterpriseUserByUserId(String userId);

    @Insert("insert into enterprise_user (ep_id, user_id, ep_user_auth,ep_user_create_at)" +
            "values(#{epId}, #{userId}, #{epUserAuth},#{epUserCreateAt})")
    int addEnterpriseUser(EnterpriseUser enterpriseUser);

    @Select("select u.* " +
            "from user u, enterprise_user eu " +
            "where u.user_id = eu.user_id  and eu.ep_id = #{ep_id} " +
            "and eu.ep_user_auth = 1 "+
            "and u.is_delete = 0 and eu.is_delete = 0")
    List<Map<String, Object>> getEmployeeListByEnterpriseId(String epId);

    @Select("SELECT * from enterprise_user " +
            "where ep_id = #{epId} and ep_user_auth = 0 " +
            "and is_delete = 0")
    EnterpriseUser getEnterpriseAdminByEnterpriseId(String epId);

    @Insert("insert into enterprise_user (ep_id, user_id, ep_user_auth,ep_user_create_at)" +
            "values(#{epId}, #{userId}, 1,now())")
    int addUserToEnterprise(String userId, String epId);

    @Update("<script> update enterprise_user\n" +
            "        set ep_user_auth = 1\n" +
            "        where ep_user_id=#{oldEpUserId};\n" +
            "\n" +
            "    update enterprise_user\n" +
            "    set ep_user_auth = 0\n" +
            "    where ep_user_id = #{newEpUserId}; </script>")

    int transferAdmin(String oldEpUserId,  String newEpUserId);

    //软删除
    @Update("update enterprise_user set is_delete = 1 where ep_user_id = #{epUserId}")
    int deleteEpUser(String epUserId);

    @Select("SELECT COUNT(*) from enterprise_user WHERE " +
            "user_id=#{userId} AND ep_id=#{epId} AND ep_user_auth=0")
    int isAdmin(String userId, String epId);


    @Select("SELECT CASE WHEN EXISTS (" +
            "SELECT 1 FROM (" +
            "  SELECT u.user_id " +
            "  FROM user u " +
            "  JOIN enterprise_user eu ON u.user_id = eu.user_id " +
            "  WHERE eu.ep_id = #{epId} " +
            "  ORDER BY u.user_influence DESC " +
            "  LIMIT #{limit}" +
            ") AS top_users " +
            "WHERE top_users.user_id = #{userId}" +
            ") THEN 1 ELSE 0 END AS isInTop")
    int isUserInTopInfluential(@Param("epId") String epId, @Param("userId") String userId, @Param("limit") int limit);

    @Select("SELECT COUNT(*) FROM enterprise_user WHERE ep_id=#{epId} AND is_delete=0")
    int totalEmployee(String epId);

    @Update("UPDATE enterprise_user SET ep_user_title=#{epUserTitle} WHERE user_id=#{userId} AND is_delete=0")
    int updateEpUserTitle(String epUserTitle, String userId);
}
