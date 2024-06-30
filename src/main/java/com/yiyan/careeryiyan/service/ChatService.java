package com.yiyan.careeryiyan.service;

import com.yiyan.careeryiyan.mapper.MessageFileMapper;
import com.yiyan.careeryiyan.mapper.MessageMapper;
import com.yiyan.careeryiyan.model.domain.Chat;
import com.yiyan.careeryiyan.model.domain.Message;
import com.yiyan.careeryiyan.model.domain.MessageFile;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import  com.yiyan.careeryiyan.mapper.ChatMapper;

import java.util.List;
import java.util.Map;

@Service
public class ChatService {
    @Resource
    private ChatMapper chatMapper;
    @Resource
    private MessageMapper messageMapper;
    @Autowired
    private MessageFileMapper messageFileMapper;

    public Chat getChatByChatId(String chatId) {
        return chatMapper.getChatByChatId(chatId);
    }

    public List<Message> getMessageListByChatId(String chatId, int msgPage, int numMsgInPage) {
        return messageMapper.getMessageListByChatId(chatId, (msgPage-1)*numMsgInPage, numMsgInPage);
    }

    public Message getMessageByMessageId(String msgReplyMsgId) {
        return messageMapper.getMessageByMessageId(msgReplyMsgId);
    }

    public List<MessageFile> getMessageFileListByMsgId(String msgId) {
        return messageFileMapper.getMessageFileListByMsgId(msgId);
    }
}
