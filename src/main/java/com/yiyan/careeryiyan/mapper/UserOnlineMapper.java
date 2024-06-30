package com.yiyan.careeryiyan.mapper;

import com.yiyan.careeryiyan.model.domain.UserOnline;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserOnlineMapper {
    @Select("SELECT * from user_online where user_online_user_id=#{userId}")
    UserOnline getUserOnlineByUserId(String userId);
}
