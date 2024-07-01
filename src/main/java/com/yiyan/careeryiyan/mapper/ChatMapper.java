package com.yiyan.careeryiyan.mapper;

import com.yiyan.careeryiyan.model.domain.Chat;
import com.yiyan.careeryiyan.model.domain.Message;
import org.apache.ibatis.annotations.*;


import java.util.List;
import java.util.Map;

@Mapper
public interface ChatMapper {
    @Select("SELECT * from chat where chat_id=#{chatId}")
    Chat getChatByChatId(String chatId);

    @Select("SELECT  * from chat c " +
            "where c.chat_user_id_1 = #{userId} and c.chat_user_1_is_delete = 0 " +
            " or c.chat_user_id_1= #{userId} and c.chat_user_2_is_delete = 0")
    List<Chat> getChatListByUserId(String userId);

    @Select("SELECT * from chat " +
            "where chat_user_id_1 = #{userId1} and chat_user_id_2 = #{userId2} or chat_user_id_1 = #{userId2} and chat_user_id_2 = #{userId1}")
    Chat getChatByUserIds(String userId1, String userId2);

    @Insert("INSERT INTO chat (chat_user_id_1, chat_user_id_2) " +
            "VALUES (#{chatUserId1}, #{chatUserId2})")
    @Options(useGeneratedKeys = true, keyProperty = "chatId")
    int addChat(Chat chat);

    @Update("UPDATE chat SET chat_user_1_is_delete = #{isDelete} " +
            "WHERE chat_id = #{chatId} and chat_user_id_1 = #{userId};" +
            "UPDATE chat SET chat_user_2_is_delete = #{isDelete} " +
            "WHERE chat_id = #{chatId} and chat_user_id_2 = #{userId}")
    int setChatIsDelete(String chatId, String userId, int isDelete);
}
