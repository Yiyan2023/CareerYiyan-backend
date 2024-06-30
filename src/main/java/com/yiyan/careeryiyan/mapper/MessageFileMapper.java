package com.yiyan.careeryiyan.mapper;

import com.yiyan.careeryiyan.model.domain.MessageFile;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface MessageFileMapper {
    @Select("SELECT * from message_file where msg_file_msg_id=#{msgId}")
    List<MessageFile> getMessageFileListByMsgId(String msgId);
}