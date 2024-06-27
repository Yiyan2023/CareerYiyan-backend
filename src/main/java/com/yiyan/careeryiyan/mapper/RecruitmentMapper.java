package com.yiyan.careeryiyan.mapper;

import com.yiyan.careeryiyan.model.domain.Apply;
import com.yiyan.careeryiyan.model.domain.Recruitment;
import com.yiyan.careeryiyan.model.request.AddRecruitmentRequest;
import com.yiyan.careeryiyan.model.request.EditRecruitmentRequest;
import com.yiyan.careeryiyan.model.response.UserApplyDetailResponse;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface RecruitmentMapper {
    @Insert("INSERT into Recruitment (enterpriseId,recruitmentName,recruitmentAddress,recruitmentTag" +
            ",minSalary,maxSalary,salaryInterval,headCount,offerCount,createTime,education,recruitmentDescription)"
            +" VALUES (#{enterpriseId},#{recruitmentName},#{recruitmentAddress}," +
            "#{recruitmentTag},#{minSalary},#{maxSalary},#{salaryInterval},#{headCount},#{offerCount},#{createTime},#{education},#{recruitmentDescription})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int addRecruitment(AddRecruitmentRequest addRecruitmentRequest);

    @Select("select * from Recruitment r where r.enterpriseId=#{enterpriseId}")
    List<Recruitment> getRecruitmentList(String enterpriseId);

    @Update("update Recruitment set recruitmentName = #{recruitmentName}, " +
            "recruitmentAddress = #{recruitmentAddress}, " +
            "recruitmentTag = #{recruitmentTag}, " +
            "minSalary = #{minSalary}, " +
            "maxSalary = #{maxSalary}, " +
            "salaryInterval = #{salaryInterval}, " +
            "education = #{education}, " +
            "recruitmentDescription = #{recruitmentDescription}, " +
            "headCount = #{headCount}, " +
            "offerCount = #{offerCount} " +
            "where id = #{id} and enterpriseId = #{enterpriseId}")
    int updateRecruitment(EditRecruitmentRequest editRecruitmentRequest);

    @Select("select * from Recruitment where id = #{id}")
    Recruitment getRecruitmentById(String id);

    @Delete("delete from Recruitment where id = #{id}")
    int deleteRecruitment(String id);

    @Select("select a.id                     as id,\n" +
            "       a.userId                 as userId,\n" +
            "       a.recruitmentId          as recruitmentId,\n" +
            "       a.status                 as status,\n" +
            "       a.cvUrl                  as cvUrl,\n" +
            "       a.createTime             as createTime,\n" +
            "       a.updateTime             as updateTime,\n" +
            "       e.id                     as enterpriseId,\n" +
            "       r.recruitmentName        as recruitmentName,\n" +
            "       r.recruitmentAddress     as recruitmentAddress,\n" +
            "       r.recruitmentTag         as recruitmentTag,\n" +
            "       r.minSalary              as minSalary,\n" +
            "       r.maxSalary              as maxSalary,\n" +
            "       r.salaryInterval         as salaryInterval,\n" +
            "       r.education              as education,\n" +
            "       r.recruitmentDescription as recruitmentDescription,\n" +
            "       r.headCount              as headCount,\n" +
            "       r.offerCount             as offerCount,\n" +
            "       e.enterpriseName         as enterpriseName,\n" +
            "       e.enterpriseAddress      as enterpriseAddress,\n" +
            "       e.enterpriseDescription  as enterpriseDescription,\n" +
            "       e.enterpriseType         as enterpriseType,\n" +
            "       e.enterpriseLicense      as enterpriseLicense,\n" +
            "       e.avatarUrl              as avatarUrl,\n" +
            "       eu.userId                as adminId,\n" +
            "       u.username               as adminName,\n" +
            "       u.avatarUrl              as adminAvatar\n" +
            "from Apply a,\n" +
            "     Recruitment r,\n" +
            "     Enterprise e,\n" +
            "     EnterpriseUser eu,\n" +
            "     User u\n"+
            "where a.userId = #{userId}\n" +
            "  and a.recruitmentId = r.id\n" +
            "  and r.enterpriseId = e.id\n" +
            "  and e.id = eu.enterpriseId\n" +
            "  and eu.role = 0\n" +
            "  and u.id = eu.userId\n")
    List<UserApplyDetailResponse> getUserApplyList(String userId);

    @Select("select * from Apply where id = #{applyId}")
    Apply getApplyById(String applyId);
}
