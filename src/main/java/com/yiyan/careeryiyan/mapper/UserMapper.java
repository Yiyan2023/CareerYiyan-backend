package com.yiyan.careeryiyan.mapper;


import com.yiyan.careeryiyan.model.domain.User;
import com.yiyan.careeryiyan.model.request.ModifyInfoRequest;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {
    @Insert("INSERT INTO User (" +
            "username, nickname, password, email, gender, salt, registerTime, avatarUrl, " +
            "blog, cv, education, enterpriseId, github, interests, position" +
            ") VALUES (" +
            "#{username}, #{nickname}, #{password}, #{email}, #{gender}, #{salt}, #{registerTime}, #{avatarUrl}, " +
            "#{blog}, #{cv}, #{education}, #{enterpriseId}, #{github}, #{interests}, #{position})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    void insertUser(User user);

    @Select("SELECT * FROM User WHERE email=#{email} ORDER BY id DESC LIMIT 1")
    User getUserByEmail(String email);

    @Select("SELECT * FROM User WHERE id=#{id}")
    User getUserById(String id);

    @Update("UPDATE User WHERE id = #{id}")
    void updateUser(User user);

    @Update("UPDATE User SET " +
            "blog = #{blog}, " +
            "cv = #{cv}, " +
            "education = #{education}, " +
            "email = #{email}, " +
            "enterpriseId = #{enterpriseId}, " +
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

}
