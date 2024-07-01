package com.yiyan.careeryiyan.mapper;


import com.yiyan.careeryiyan.model.domain.User;
import com.yiyan.careeryiyan.model.domain.UserRecruitmentPreferences;
import com.yiyan.careeryiyan.model.request.ModifyInfoRequest;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

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

    @Update("UPDATE user WHERE id = #{id}")
    void updateUser(User user);

    @Update("UPDATE user SET " +
            "user_blog_url = #{userBlogUrl}, " +
            "user_cv_url = #{userCvUrl}, " +
            "user_edu = #{userEdu}, " +
            "user_email = #{userEmail}, " +
            "user_gender = #{userGender}, " +
            "user_github_url = #{userGithubUrl}, " +
            "user_interest = #{userInterest}, " +
            "user_nickname = #{userNickname}, " +
            "user_name = #{userName} " +
            "WHERE user_id = #{userId}")
    int modifyUser(User user);


    @Update("UPDATE user SET user_avatar_url=#{avatarUrl, jdbcType=VARCHAR}  WHERE user_id = #{id}")
    int updateAvatarUrl(String avatarUrl,String id);

    @Update("UPDATE user SET user_cv_url=#{CV, jdbcType=VARCHAR}  WHERE user_id = #{id}")
    int updateCV(String CV,String id);

    @Select("SELECT u.user_name AS userName, " +
            "u.user_nickname AS userNickname, " +
            "u.user_id AS userId, " +
            "u.user_avatar_url AS userAvatarUrl, " +
            "u.user_email AS userEmail " +
            "FROM user u " +
            "WHERE user_id = #{userId}")
    Map<String, Object> getUserInfoById(String userId);

    @Select("select rc_tag from user_recruitment_preferences where user_id=#{userId}")
    List<String> getUserRcTags(String userId);

    @Update("UPDATE user SET user_influence=(user_influence + #{influence}) WHERE user_id=#{userId}")
    void updateInfluence(int influence,String userId);

    @Update("UPDATE user SET is_delete = 1, " +
            "user_name = '用户已注销'," +
            "user_nickname = '用户已注销'," +
            "user_reg_at = null," +
            "user_email = 'null'," +
            "user_edu = null," +
            "user_interest = null," +
            "user_cv_url = null," +
            "user_addr = null," +
            "user_github_url = null," +
            "user_blog_url = null," +
            "user_influence = 0," +
            "user_avatar_url = 'https://career-yiyan.oss-cn-beijing.aliyuncs.com/avatar/logged_out_avatar.png' " +
            "WHERE user_id=#{userId}")
    int deleteUser(String userId);

}
