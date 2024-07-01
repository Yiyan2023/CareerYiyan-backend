package com.yiyan.careeryiyan.mapper;

import com.yiyan.careeryiyan.model.domain.FollowEnterprise;
import com.yiyan.careeryiyan.model.domain.FollowUser;
import com.yiyan.careeryiyan.model.domain.User;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

@Mapper
public interface FollowMapper {

    @Insert("INSERT INTO follow_user (user_id, following_user_id) VALUES (#{userId}, #{targetUserId})")
    int insertFollowUser(@Param("userId") String userId, @Param("targetUserId") String targetUserId);

    @Select("SELECT * FROM follow_user where user_id =#{userId} and following_user_id =#{targetUserId} ")
    FollowUser getFollowUser(@Param("userId") String userId, @Param("targetUserId") String targetUserId);

    @Select("SELECT * FROM follow_enterprise where user_id =#{userId} and ep_id =#{targetUserId} ")
    FollowEnterprise getFollowEp(@Param("userId") String userId, @Param("targetUserId") String targetUserId);
    @Update("UPDATE follow_user SET is_delete = #{isDelete} WHERE user_id = #{userId} AND following_user_id = #{targetUserId}")
    int updateFollowUser(@Param("userId") String userId, @Param("targetUserId") String targetUserId, @Param("isDelete") boolean isDelete);

    @Insert("INSERT INTO follow_enterprise (user_id, ep_id) VALUES (#{userId}, #{epId})")
    int insertFollowEnterprise(@Param("userId") String userId, @Param("epId") String epId);

    @Update("UPDATE follow_enterprise SET is_delete = #{isDelete} WHERE user_id = #{userId} AND ep_id = #{epId}")
    int updateFollowEnterprise(@Param("userId") String userId, @Param("epId") String epId, @Param("isDelete") boolean isDelete);

    @Select("SELECT  u.*" +
            "FROM user u " +
            "INNER JOIN follow_user fu ON u.user_id = fu.following_user_id " +
            "WHERE fu.user_id = #{userId} AND fu.is_delete = 0")
    List<User> getFollowingUsers(@Param("userId") String userId);

    @Select("select e.*,e.ep_id as ep_id " +
            "FROM enterprise e " +
            "INNER JOIN follow_enterprise fe ON e.ep_id = fe.ep_id " +
            "WHERE fe.user_id = #{userId} AND fe.is_delete = 0")
    List<Map<String, Object>> getFollowingEnterprises(@Param("userId") String userId);

    @Select("SELECT u.user_id AS userId, u.user_name AS userName, u.user_nickname AS userNickname, " +
            "u.user_avatar_url AS userAvatarUrl, u.user_gender AS userGender, u.user_email AS userEmail, " +
            "DATE_FORMAT(u.user_reg_at, '%Y-%m-%d %H:%i:%s') AS userRegAt"  +
            "FROM user u INNER JOIN follow_user fu ON u.user_id = fu.user_id WHERE fu.follow_user_id = #{userId} AND fu.is_delete = 0")
    List<Map<String, Object>> getUserFollowers(@Param("userId") String userId);

    @Select("SELECT u.user_id AS userId, " +
            "u.user_name AS userName, " +
            "u.user_nickname AS userNickname, " +
            "u.user_avatar_url AS userAvatarUrl, " +
            "u.user_gender AS userGender, " +
            "u.user_email AS userEmail, " +
            "DATE_FORMAT(u.user_reg_at, '%Y-%m-%d %H:%i:%s') AS userRegAt " +
            "FROM user u " +
            "INNER JOIN follow_enterprise fe ON u.user_id = fe.user_id " +
            "WHERE fe.ep_id = #{epId} AND fe.is_delete = 0")
    List<Map<String, Object>> getEnterpriseFollowers(@Param("epId") String epId);



}