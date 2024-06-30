package com.yiyan.careeryiyan.mapper;

import com.yiyan.careeryiyan.model.domain.Chat;
import com.yiyan.careeryiyan.model.domain.Message;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;


import java.util.List;
import java.util.Map;

@Mapper
public interface ChatMapper {
    @Select("SELECT * from chat where chat_id=#{chatId}")
    Chat getChatByChatId(String chatId);

}
