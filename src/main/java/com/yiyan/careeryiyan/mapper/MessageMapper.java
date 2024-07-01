package com.yiyan.careeryiyan.mapper;

import com.yiyan.careeryiyan.model.domain.Message;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface MessageMapper {

    @Select("SELECT * " +
            "from message m " +
            "where m.msg_chat_id=#{chatId} " +
            "order by m.msg_create_at desc limit #{filter}, #{numMsgInPage}")
    List<Message> getMessageListByChatId(String chatId, int filter, int numMsgInPage);

    @Select("SELECT * from message where msg_id=#{msgReplyMsgId}")
    Message getMessageByMessageId(String msgReplyMsgId);

    @Select("SELECT * from message m " +
            "where m.msg_chat_id=#{chat_Id}" +
            "order by m.msg_create_at desc limit 1")
    Message getLastMessageInChat(String chatId);

    @Select("SELECT count(*) from message m " +
            "where m.msg_chat_id=#{chatId} and m.msg_is_read=0 and m.msg_send_user_id!=#{userId}")
    int getUnreadCount(String chatId, String userId);

    @Insert("INSERT INTO message(msg_id, msg_chat_id, msg_send_user_id, msg_content, msg_create_at, msg_reply_msg_id, " +
            "msg_create_date, msg_create_time, msg_is_system, msg_is_read) " +
            "VALUES(#{msgId}, #{msgChatId}, #{msgSendUserId}, #{msgContent}, #{msgCreateAt}, #{msgReplyMsgId}, " +
            "#{msgCreateDate}, #{msgCreateTime}, #{msgIsSystem}, #{msgIsRead})")
    @Options(useGeneratedKeys = true, keyProperty = "msgId")
    int addMessage(Message message);

    @Select("SELECT count(*) from message m ,chat c " +
            "where m.msg_chat_id= c.chat_id and (c.chat_user_id_1 = #{userId} or c.chat_user_id_2 = #{userId}) " +
            "and m.msg_is_read=0 and m.msg_send_user_id!=#{userId} ")
    int getTotalUnreadCount(String userId);

    @Update("UPDATE message set msg_is_read=1 where msg_chat_id=#{chatId} and msg_send_user_id!=#{userId}")
    void setChatUserSendMessageRead(String chatId, String userId);
}
