package com.yiyan.careeryiyan.mapper;

import com.yiyan.careeryiyan.model.domain.Enterprise;
import com.yiyan.careeryiyan.model.request.SearchEnterpriseRequest;
import com.yiyan.careeryiyan.model.request.SearchRecruitmentRequest;
import com.yiyan.careeryiyan.model.request.SearchUserRequest;
import com.yiyan.careeryiyan.model.response.RecruitmentDetailResponse;
import com.yiyan.careeryiyan.model.response.UserDetailResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SearchMapper {


    @Select("SELECT\n" +
            "    r.id AS recruitmentId,\n" +
            "    r.enterpriseId AS enterpriseId,\n" +
            "    r.recruitmentName AS recruitmentName,\n" +
            "    r.recruitmentAddress AS recruitmentAddress,\n" +
            "    r.recruitmentTag AS recruitmentTag,\n" +
            "    r.minSalary AS minSalary,\n" +
            "    r.maxSalary AS maxSalary,\n" +
            "    r.salaryInterval AS salaryInterval,\n" +
            "    r.education AS education,\n" +
            "    r.recruitmentDescription AS recruitmentDescription,\n" +
            "    r.createTime AS createTime,\n" +
            "    r.headCount AS headCount,\n" +
            "    r.offerCount AS offerCount,\n" +
            "    e.enterpriseName AS enterpriseName,\n" +
            "    e.enterpriseAddress AS enterpriseAddress,\n" +
            "    e.enterpriseDescription AS enterpriseDescription,\n" +
            "    e.enterpriseType AS enterpriseType,\n" +
            "    e.enterpriseLicense AS enterpriseLicense,\n" +
            "    e.avatarUrl AS avatarUrl,\n" +
            "    MATCH(r.recruitmentName) AGAINST(#{recruitmentName} IN NATURAL LANGUAGE MODE) AS score\n" +
            "FROM Recruitment r\n" +
            "JOIN Enterprise e ON r.enterpriseId = e.id\n" +
            "WHERE MATCH(r.recruitmentName) AGAINST(#{recruitmentName} IN NATURAL LANGUAGE MODE)\n" +
            "ORDER BY score DESC\n" +
            "LIMIT #{offset}, #{pageSize};"
    )
    List<RecruitmentDetailResponse> searchRecruitment(SearchRecruitmentRequest searchRecruitmentRequest);

    //单表查询，不需要手动映射名字
    @Select("SELECT\n" +
            "*,\n" +
            "MATCH(enterpriseName) AGAINST(#{enterpriseName}) AS score\n" +
            "FROM Enterprise\n" +
            "WHERE MATCH(enterpriseName) AGAINST(#{enterpriseName} IN NATURAL LANGUAGE MODE)\n" +
            "ORDER BY score DESC\n" +
            "LIMIT #{offset}, #{pageSize};")
    List<Enterprise> searchEnterprise(SearchEnterpriseRequest searchEnterpriseRequest);

    @Select("\n" +
            "SELECT\n" +
            "    u.id AS userId,\n" +
            "    u.username AS username,\n" +
            "    u.nickname AS nickname,\n" +
            "    u.password AS password,\n" +
            "    u.email AS email,\n" +
            "    u.gender AS gender,\n" +
            "    u.salt AS salt,\n" +
            "    u.registerTime AS registerTime,\n" +
            "    u.avatarUrl AS userAvatarUrl,\n" +
            "    u.blog AS blog,\n" +
            "    u.cv AS cv,\n" +
            "    u.education AS education,\n" +
            "    e.enterpriseName AS enterpriseName,\n" +
            "    u.github AS github,\n" +
            "    u.interests AS interests,\n" +
            "    u.position AS position,\n" +
            "    e.enterpriseAddress AS enterpriseAddress,\n" +
            "    e.enterpriseDescription AS enterpriseDescription,\n" +
            "    e.enterpriseType AS enterpriseType,\n" +
            "    e.enterpriseLicense AS enterpriseLicense,\n" +
            "    e.avatarUrl AS enterpriseAvatarUrl,\n" +
            "    MATCH(u.username) AGAINST(#{userName}) AS score\n" +
            "FROM User u,Enterprise e,EnterpriseUser eu\n" +
            "where u.id = eu.userId and eu.enterpriseId = e.id\n" +
            "and MATCH(u.username) AGAINST(#{userName} IN NATURAL LANGUAGE MODE)\n" +
            "ORDER BY score DESC\n" +
            "LIMIT #{offset}, #{pageSize};")
    List<UserDetailResponse> searchUser(SearchUserRequest searchUserRequest);

    @Select("SELECT count(*)\n" +
            "FROM User u,Enterprise e,EnterpriseUser eu\n" +
            "where u.id = eu.userId and eu.enterpriseId = e.id\n" +
            "and MATCH(u.username) AGAINST(#{userName} IN NATURAL LANGUAGE MODE)\n")
    int getSearchUserTotal(SearchUserRequest searchUserRequest);

    @Select("SELECT count(*)\n" +
            "FROM Enterprise\n" +
            "WHERE MATCH(enterpriseName) AGAINST(#{enterpriseName} IN NATURAL LANGUAGE MODE)\n")
    int getSearchEnterpriseTotal(SearchEnterpriseRequest searchEnterpriseRequest);

    @Select("SELECT count(*)\n" +
            "FROM Recruitment r\n" +
            "JOIN Enterprise e ON r.enterpriseId = e.id\n" +
            "WHERE MATCH(r.recruitmentName) AGAINST(#{recruitmentName} IN NATURAL LANGUAGE MODE)\n")
    int getSearchRecruitmentTotal(SearchRecruitmentRequest searchRecruitmentRequest);
}
