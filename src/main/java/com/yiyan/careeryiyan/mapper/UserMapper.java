package com.yiyan.careeryiyan.mapper;


import com.yiyan.careeryiyan.model.domain.User;
import com.yiyan.careeryiyan.model.domain.UserRecruitmentPreferences;
import com.yiyan.careeryiyan.model.request.ModifyInfoRequest;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {
    @Insert("INSERT INTO user (" +
            "user_name, user_pwd, user_email, user_gender, user_salt, user_reg_at, user_avatar_url, " +
            "user_nickname, user_edu, user_interest, user_cv_url, user_addr, user_github_url, user_blog_url" +
            ") VALUES (" +
            "#{userName}, #{userPwd}, #{userEmail}, #{userGender}, #{userSalt}, #{userRegAt}, #{userAvatarUrl}, " +
            "#{userNickname}, #{userEdu}, #{userInterest}, #{userCvUrl}, #{userAddr}, #{userGithubUrl}, #{userBlogUrl}" +
            ")")
    @Options(useGeneratedKeys = true, keyProperty = "userId", keyColumn = "user_id")
    void insertUser(User user);

    @Update("UPDATE user SET user_influence=influence WHERE user_id=#{userId}")
    void updateInfluence(int influence,String userId);


    @Select("SELECT * FROM user WHERE user_email=#{email} ORDER BY user_id DESC LIMIT 1")
    User getUserByEmail(String email);

    @Select("SELECT * FROM user WHERE user_id=#{id}")
    User getUserById(String id);

    @Update("UPDATE user WHERE user_id = #{userId}")
    void updateUser(User user);

    @Update("UPDATE user SET " +
            "user_blog_url = #{userBlogUrl}, " +
            "user_cv_url = #{userCvUrl}, " +
            "user_edu = #{userEdu}, " +
            "user_email = #{userEmail}, " +
            "user_gender = #{userGender}, " +
            "user_github_url = #{userGithubUrl}, " +
            "user_interests = #{userInterests}, " +
            "user_nickname = #{usernickname}, " +
            "user_name = #{userName} " +
            "WHERE user_id = #{id}")
    int modifyUser(ModifyInfoRequest request);


    @Update("UPDATE user SET user_avatar_url=#{avatarUrl, jdbcType=VARCHAR}  WHERE user_id = #{id}")
    int updateAvatarUrl(String avatarUrl,String id);

    @Update("UPDATE user SET user_cv_url=#{cvUrl, jdbcType=VARCHAR}  WHERE user_id = #{userId}")
    int updateCV(String cvUrl,String userId);

    @Select("SELECT * FROM user_recruitment_preferences WHERE user_id=#{userId}")
    List<UserRecruitmentPreferences> getUserRecruitmentPreferences(String userId);
}
