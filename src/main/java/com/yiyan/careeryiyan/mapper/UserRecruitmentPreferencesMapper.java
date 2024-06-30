package com.yiyan.careeryiyan.mapper;

import com.yiyan.careeryiyan.model.domain.UserRecruitmentPreferences;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserRecruitmentPreferencesMapper {

    @Select("SELECT * FROM user_recruitment_preferences WHERE user_id=#{userId}")
    List<UserRecruitmentPreferences> getUserRecruitmentPreferences(String userId);

    @Insert("INSERT INTO user_recruitment_preferences(user_id, rc_tag) VALUES(#{userId}, #{recruitmentTag})")
    @Options(useGeneratedKeys = true, keyColumn = "user_rc_pref_id")
    int insertUserRecruitmentPreferences(String userId, String recruitmentTag);
    @Delete("DELETE FROM user_recruitment_preferences WHERE user_id = #{userId}")
    void deleteUserRecruitmentPreferences(String userId);
}