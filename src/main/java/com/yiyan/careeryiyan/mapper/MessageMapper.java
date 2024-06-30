package com.yiyan.careeryiyan.mapper;

import com.yiyan.careeryiyan.model.domain.Message;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

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
}
