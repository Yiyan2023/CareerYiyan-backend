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

    @Update("UPDATE Apply set status = #{state} where id = #{applyId}")
    int changeState(String applyId, String state);

    @Select("SELECT  * from Apply where recruitmentId = #{recruitmentId}")
    List<Apply> getApplyByRecruitmentId(String recruitmentId);
}
