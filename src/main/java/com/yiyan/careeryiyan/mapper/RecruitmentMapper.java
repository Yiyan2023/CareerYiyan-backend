package com.yiyan.careeryiyan.mapper;

import com.yiyan.careeryiyan.model.domain.Apply;
import com.yiyan.careeryiyan.model.domain.Recruitment;
import com.yiyan.careeryiyan.model.request.AddRecruitmentRequest;
import com.yiyan.careeryiyan.model.request.EditRecruitmentRequest;
//import com.yiyan.careeryiyan.model.response.UserApplyDetailResponse;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

@Mapper
public interface RecruitmentMapper {
    @Insert("insert into recruitment (rc_name, rc_addr, rc_tag, rc_min_salary, " +
            "rc_max_salary, rc_salary_count, rc_edu, rc_desc, rc_total_count, " +
            "rc_offer_count, rc_create_at, ep_id) \n"
            +" VALUES (#{rcName},#{rcAddr},#{rcTag}," +
            "#{rcMinSalary},#{rcMaxSalary},#{rcSalaryCount},#{rcEdu}," +
            "#{rcDesc},#{rcTotalCount},#{rcOfferCount},#{rcCreateAt},#{epId})")
    @Options(useGeneratedKeys = true, keyProperty = "rcId")
    int addRecruitment(AddRecruitmentRequest addRecruitmentRequest);

//    @Select("select * from recruitment  where ep_id=#{epId} and is_delete=0")
//    List<Recruitment> getRecruitmentList(String epId);

    //一个epId下所有recruitment和其admin的信息
    @Select("select r.*,\n" +
            "       u.user_id as hrId,u.user_name as hrName,\n" +
            "       u.user_avatar_url as hrAvatarUrl, u.user_gender as hrGender\n" +
            "       from recruitment r,enterprise_user eu,user u\n" +
            "where eu.ep_id=#{epId} and r.ep_id=eu.ep_id and eu.ep_user_auth=0 and\n" +
            "      eu.user_id=u.user_id\n" +
            "  and  r.is_delete=0 and eu.is_delete=0 and u.is_delete=0;")
    List<Map<String,Object>> getRecruitmentList(String epId);

    //recruitment及其admin的信息
    @Select("select r.*,\n" +
            "       u.user_id as hrId,u.user_name as hrName,\n" +
            "       u.user_avatar_url as hrAvatarUrl, u.user_gender as hrGender\n" +
            "       from recruitment r,enterprise_user eu,user u\n" +
            "where r.rc_id=#{rcId} and r.ep_id=eu.ep_id and eu.ep_user_auth=0 and\n" +
            "      eu.user_id=u.user_id\n" +
            "  and  r.is_delete=0 and eu.is_delete=0 and u.is_delete=0;")
    Map<String, Object> getRecruitmentInfo(String rcId);

//    @Update("update Recruitment set recruitmentName = #{recruitmentName}, " +
//            "recruitmentAddress = #{recruitmentAddress}, " +
//            "recruitmentTag = #{recruitmentTag}, " +
//            "minSalary = #{minSalary}, " +
//            "maxSalary = #{maxSalary}, " +
//            "salaryInterval = #{salaryInterval}, " +
//            "education = #{education}, " +
//            "recruitmentDescription = #{recruitmentDescription}, " +
//            "headCount = #{headCount}, " +
//            "offerCount = #{offerCount} " +
//            "where id = #{id} and enterpriseId = #{enterpriseId}")
    //参考addRecruitment写updateRecruitment
    @Update("update recruitment set rc_name = #{rcName}, " +
            "rc_addr = #{rcAddr}, " +
            "rc_tag = #{rcTag}, " +
            "rc_min_salary = #{rcMinSalary}, " +
            "rc_max_salary = #{rcMaxSalary}, " +
            "rc_salary_count = #{rcSalaryCount}, " +
            "rc_edu = #{rcEdu}, " +
            "rc_desc = #{rcDesc}, " +
            "rc_total_count = #{rcTotalCount}, " +
            "rc_offer_count = #{rcOfferCount}  " +
            "where rc_id = #{rcId} and is_delete = 0")
    int updateRecruitment(EditRecruitmentRequest editRecruitmentRequest);

    @Select("select * from recruitment where rc_id = #{rcId} and is_delete = 0")
    Recruitment getRecruitmentById(String id);

    @Delete("delete from recruitment where rc_id = #{rcId}")
    int deleteRecruitment(String id);

    @Select("select *,a.user_id as adminId,a.rc_id as rcId,e.ep_id as epId from\n" +
            "user u ,apply a,recruitment r,enterprise e, enterprise_user eu\n" +
            "where a.user_id= #{userId}\n" +
            "and a.rc_id = r.rc_id\n" +
            "and r.ep_id = e.ep_id\n" +
            "and e.ep_id = eu.ep_id\n" +
            "and eu.ep_user_auth=0\n" +
            "and u.user_id = eu.user_id\n" +
            "and u.is_delete =0 and a.is_delete=0 and r.is_delete=0 and e.is_delete=0 and eu.is_delete=0\n" +
            ";")
    List<Map<String,Object>> getUserApplyList(String userId);

    @Select("select * from apply where apply_id = #{applyId}")
    Apply getApplyById(String applyId);


}
