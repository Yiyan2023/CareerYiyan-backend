package com.yiyan.careeryiyan.mapper;


import com.yiyan.careeryiyan.model.domain.User;
import com.yiyan.careeryiyan.model.domain.UserJobPreferences;
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

    @Select("SELECT * FROM user WHERE user_email=#{email} ORDER BY user_id DESC LIMIT 1")
    User getUserByEmail(String email);

    @Select("SELECT * FROM user WHERE user_id=#{id}")
    User getUserById(String id);

    @Update("UPDATE User WHERE id = #{id}")
    void updateUser(User user);

    @Update("UPDATE User SET " +
            "blog = #{blog}, " +
            "cv = #{cv}, " +
            "education = #{education}, " +
            "email = #{email}, " +
            "gender = #{gender}, " +
            "github = #{github}, " +
            "interests = #{interests}, " +
            "nickname = #{nickname}, " +
            "position = #{position}, " +
            "username = #{username} " +
            "WHERE id = #{id}")
    int modifyUser(ModifyInfoRequest request);


    @Update("UPDATE User SET avatarUrl=#{avatarUrl, jdbcType=VARCHAR}  WHERE id = #{id}")
    int updateAvatarUrl(String avatarUrl,String id);

    @Update("UPDATE User SET CV=#{CV, jdbcType=VARCHAR}  WHERE id = #{id}")
    int updateCV(String CV,String id);

    @Select("SELECT * FROM UserJobPreferences WHERE userId=#{userId}")
    List<UserJobPreferences> getUserJobPreferences(String userId);
}
