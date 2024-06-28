package com.yiyan.careeryiyan.mapper;

import com.yiyan.careeryiyan.model.domain.Apply;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface ApplyMapper {
    @Insert("INSERT into apply(user_id, rc_id, apply_status, apply_cv_url, apply_create_at, apply_update_at) " +
            "values(#{userId}, #{rcId}, #{applyStatus}, #{applyCvUrl}, #{applyCreateAt}, #{applyUpdateAt})")
    int addApply(Apply apply);

    @Select("SELECT * from apply where user_Id = #{userId} and rc_id = #{recruitmentId}")
    Apply getApplyByUserIdAndRecruitmentId(String userId, String recruitmentId);


    @Select("SELECT  * from apply where rc_id = #{recruitmentId}")
    List<Apply> getApplyByRecruitmentId(String recruitmentId);


    @Select("SELECT a.*, r.rc_offer_count "
            + "FROM apply a "
            + "JOIN recruitment r ON a.rc_id = r.rc_id "
            + "WHERE a.apply_id = #{applyId} FOR UPDATE")
    Apply getApplyAndRecruitmentForUpdate(String applyId);

    @Update("UPDATE apply SET apply_status = #{status} WHERE apply_id = #{applyId}")
    int changeStatus(String applyId, int status);

    @Update("UPDATE recruitment rc "
            + "JOIN apply a ON rc.rc_id = a.rc_id "
            + "SET rc.rc_offer_count = rc.rc_offer_count + 1, a.apply_status = #{status} "
            + "WHERE a.apply_id = #{applyId}")
    int increaseOfferCountAndChangeStatus(String applyId, int status);

    @Update("UPDATE recruitment rc "
            + "JOIN apply a ON rc.rc_id = a.rc_id "
            + "SET rc.rc_offer_count = rc.rc_offer_count - 1, a.apply_status = #{status} "
            + "WHERE a.apply_id = #{applyId}")
    int decreaseOfferCountAndChangeStatus(String applyId, int status);

    @Update("UPDATE recruitment rc "
            + "JOIN apply a ON rc.rc_id = a.rc_id "
            + "SET rc.rc_accept_count = rc.rc_accept_count + 1, a.apply_status = #{status} "
            + "WHERE a.apply_id = #{applyId}")
    int increaseAcceptCountAndChangeStatus(String applyId, int status);
}
