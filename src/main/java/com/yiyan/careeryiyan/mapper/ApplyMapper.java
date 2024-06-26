package com.yiyan.careeryiyan.mapper;

import com.yiyan.careeryiyan.model.domain.Apply;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface ApplyMapper {
    @Insert("INSERT into Apply(userId, recruitmentId, status, cvUrl, createTime, updateTime) values(#{userId}, #{recruitmentId}, #{status}, #{cvUrl}, #{createTime}, #{updateTime})")
    int addApply(Apply apply);

    @Select("SELECT * from Apply where userId = #{userId} and recruitmentId = #{recruitmentId}")
    Apply getApplyByUserIdAndRecruitmentId(String userId, String recruitmentId);


    @Select("SELECT  * from Apply where recruitmentId = #{recruitmentId}")
    List<Apply> getApplyByRecruitmentId(String recruitmentId);


    @Select("SELECT a.*, r.offerCount "
            + "FROM Apply a "
            + "JOIN Recruitment r ON a.recruitmentId = r.id "
            + "WHERE a.id = #{applyId} FOR UPDATE")
    Apply getApplyAndRecruitmentForUpdate(String applyId);

    @Update("UPDATE Apply SET status = #{status} WHERE id = #{applyId}")
    int changeStatus(String applyId, int status);

    @Update("UPDATE Recruitment r "
            + "JOIN Apply a ON r.id = a.recruitmentId "
            + "SET r.offerCount = r.offerCount + 1, a.status = #{status} "
            + "WHERE a.id = #{applyId}")
    int increaseOfferCountAndChangeStatus(String applyId, int status);

    @Update("UPDATE Recruitment r "
            + "JOIN Apply a ON r.id = a.recruitmentId "
            + "SET r.offerCount = r.offerCount - 1, a.status = #{status} "
            + "WHERE a.id = #{applyId}")
    int decreaseOfferCountAndChangeStatus(String applyId, int status);
}
