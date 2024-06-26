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

    @Select("select a.id as id, a.userId as userId, a.recruitmentId as recruitmentId, a.status as status, a.cvUrl as cvUrl, a.createTime as createTime, a.updateTime as updateTime, " +
            "r.id as enterpriseId, r.recruitmentName as recruitmentName, r.recruitmentAddress as recruitmentAddress, r.recruitmentTag as recruitmentTag, r.minSalary as minSalary, r.maxSalary as maxSalary, r.salaryInterval as salaryInterval, r.education as education, r.recruitmentDescription as recruitmentDescription, r.headCount as headCount, r.offerCount as offerCount, " +
            "e.enterpriseName as enterpriseName, e.enterpriseAddress as enterpriseAddress, e.enterpriseDescription as enterpriseDescription, e.enterpriseType as enterpriseType, e.enterpriseLicense as enterpriseLicense, e.avatarUrl as avatarUrl " +
            "from Apply a, Recruitment r, Enterprise e " +
            "where a.userId = #{id} and a.recruitmentId = r.id and r.enterpriseId = e.id")
    List<UserApplyDetailResponse> getUserApplyList(String id);

    @Select("select * from Apply where id = #{applyId}")
    Apply getApplyById(String applyId);
}
