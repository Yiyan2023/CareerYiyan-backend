package com.yiyan.careeryiyan.mapper;

import com.yiyan.careeryiyan.model.domain.UserOnline;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserOnlineMapper {
    @Select("SELECT * from user_online where user_online_user_id=#{userId}")
    UserOnline getUserOnlineByUserId(String userId);

    @Insert("INSERT INTO user_online(user_online_user_id, user_online_status, user_online_last_change_at, user_online_chat_id)" +
            " VALUES(#{userOnlineUserId}, #{userOnlineStatus}, #{userOnlineLastChangeAt}, #{userOnlineChatId})")
    void addUserOnline(UserOnline userOnline);

    @Update("UPDATE user_online SET user_online_status=#{userOnlineStatus}, user_online_last_change_at=#{userOnlineLastChangeAt}, user_online_chat_id=#{userOnlineChatId} WHERE user_online_user_id=#{userOnlineUserId}")
    void updateUserOnline(UserOnline userOnline);
    //user_online_chat_id
    //user_online_last_change_at
    //user_online_user_id
    //user_online_status
}
