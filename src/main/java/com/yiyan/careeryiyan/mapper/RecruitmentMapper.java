package com.yiyan.careeryiyan.mapper;

import com.yiyan.careeryiyan.model.request.AddRecruitmentRequest;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RecruitmentMapper {
    @Insert("INSERT into Recruitment (enterpriseId,recruitmentName,recruitmentAddress,recruitmentTag" +
            ",minSalary,maxSalary,salaryInterval,headCount,offerCount,createTime,education,recruitmentDescription)"
            +" VALUES (#{enterpriseId},#{recruitmentName},#{recruitmentAddress}," +
            "#{recruitmentTag},#{minSalary},#{maxSalary},#{salaryInterval},#{headCount},#{offerCount},#{createTime},#{education},#{recruitmentDescription})")
    int addRecruitment(AddRecruitmentRequest addRecruitmentRequest);
}
