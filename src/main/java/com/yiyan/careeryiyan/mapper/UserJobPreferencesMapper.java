package com.yiyan.careeryiyan.mapper;

import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserJobPreferencesMapper {

    @Select("SELECT recruitmentTag FROM UserJobPreferences WHERE user_id = #{userId}")
    List<String> getUserJobPreferencesByUserId(int userId);

    @Insert("INSERT INTO UserJobPreferences(user_id, recruitmentTag) VALUES(#{userId}, #{recruitmentTag})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertUserJobPreferences(String userId, String recruitmentTag);
    @Delete("DELETE FROM UserJobPreferences WHERE user_id = #{userId}")
    void deleteUserJobPreferences(String userId);
}