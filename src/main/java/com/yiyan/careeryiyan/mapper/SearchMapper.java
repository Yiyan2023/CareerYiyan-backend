package com.yiyan.careeryiyan.mapper;

import com.yiyan.careeryiyan.model.domain.Enterprise;
import com.yiyan.careeryiyan.model.request.SearchEnterpriseRequest;
import com.yiyan.careeryiyan.model.request.SearchRecruitmentRequest;
import com.yiyan.careeryiyan.model.request.SearchUserRequest;
//import com.yiyan.careeryiyan.model.response.RecruitmentDetailResponse;
import com.yiyan.careeryiyan.model.response.UserDetailResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface SearchMapper {


    @Select("select *,r.ep_id as ep_id ,\n" +
            "       MATCH(r.rc_name) AGAINST(#{rcName} IN NATURAL LANGUAGE MODE) as score\n" +
            "from recruitment r join enterprise e on r.ep_id = e.ep_id\n" +
            "         where e.is_delete=0 and r.is_delete=0 and MATCH(r.rc_name) AGAINST(#{rcName} IN NATURAL LANGUAGE MODE)\n" +
            "order by score desc\n" +
            "limit #{offset},#{pageSize};"
    )
    List<Map<String,Object>> searchRecruitment(SearchRecruitmentRequest searchRecruitmentRequest);

    @Select("select count(*)\n" +
            "from recruitment r join enterprise e on r.ep_id = e.ep_id\n" +
            "         where e.is_delete=0 and r.is_delete=0 and MATCH(r.rc_name) AGAINST(#{rcName} IN NATURAL LANGUAGE MODE);")
    int getSearchRecruitmentTotal(SearchRecruitmentRequest searchRecruitmentRequest);

    //单表查询，不需要手动映射名字
    @Select("select * ,MATCH(ep_name) AGAINST(#{epName} IN NATURAL LANGUAGE MODE) as score\n" +
            "from enterprise\n" +
            "where MATCH(ep_name) AGAINST(#{epName} IN NATURAL LANGUAGE MODE) and is_delete=0\n" +
            "order by score desc\n" +
            "limit #{offset},#{pageSize};")
    List<Map<String,Object>>  searchEnterprise(SearchEnterpriseRequest searchEnterpriseRequest);

    @Select("select count(*)\n" +
            "from enterprise\n" +
            "where MATCH(ep_name) AGAINST(#{epName} IN NATURAL LANGUAGE MODE) and  is_delete=0;")
    int getSearchEnterpriseTotal(SearchEnterpriseRequest searchEnterpriseRequest);

    @Select("select *,u.user_id as user_id,e.ep_id=eu.ep_id as ep_id," +
            "MATCH(user_nickname) AGAINST(#{userNickname} IN NATURAL LANGUAGE MODE) as score\n" +
            "from user u join enterprise_user eu on u.user_id = eu.user_id join enterprise e on eu.ep_id = e.ep_id\n" +
            "where MATCH(user_nickname) AGAINST(#{userNickname} IN NATURAL LANGUAGE MODE) " +
            "and u.is_delete=0 and e.is_delete=0 and eu.is_delete=0\n" +
            "ORDER BY score DESC\n" +
            "limit #{offset},#{pageSize};")
    List<Map<String,Object>>  searchUser(SearchUserRequest searchUserRequest);

    @Select("select count(*)\n" +
            "from user u join enterprise_user eu on u.user_id = eu.user_id join enterprise e on eu.ep_id = e.ep_id\n" +
            "where u.is_delete=0 and e.is_delete=0 and eu.is_delete=0\n" +
            "and MATCH(user_nickname) AGAINST(#{userNickname} IN NATURAL LANGUAGE MODE);")
    int getSearchUserTotal(SearchUserRequest searchUserRequest);




}
