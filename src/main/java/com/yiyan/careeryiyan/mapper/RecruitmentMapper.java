package com.yiyan.careeryiyan.mapper;

import com.yiyan.careeryiyan.model.domain.Recruitment;
import com.yiyan.careeryiyan.model.request.AddRecruitmentRequest;
import com.yiyan.careeryiyan.model.request.EditRecruitmentRequest;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

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
}
