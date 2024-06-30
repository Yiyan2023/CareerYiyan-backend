package com.yiyan.careeryiyan.mapper;

import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserJobPreferencesMapper {

    @Select("SELECT rc_tag FROM user_recruitment_preferences WHERE user_id = #{userId} AND is_delete = 0")
    List<String> getUserJobPreferencesByUserId(int userId);

    @Insert("INSERT INTO user_recruitment_preferences(user_id, rc_tag) VALUES(#{userId}, #{recruitmentTag})")
    @Options(useGeneratedKeys = true, keyColumn = "user_rc_pref_id")
    int insertUserJobPreferences(String userId, String recruitmentTag);
    @Delete("DELETE FROM user_recruitment_preferences WHERE user_id = #{userId}")
    void deleteUserJobPreferences(String userId);
}