package com.yiyan.careeryiyan.controller;

import com.yiyan.careeryiyan.exception.BaseException;
import com.yiyan.careeryiyan.model.domain.Chat;
import com.yiyan.careeryiyan.model.domain.Message;
import com.yiyan.careeryiyan.model.domain.MessageFile;
import com.yiyan.careeryiyan.model.domain.User;
import com.yiyan.careeryiyan.model.request.FetchMessagesRequest;
import com.yiyan.careeryiyan.service.ChatService;
import com.yiyan.careeryiyan.service.MyWebSocket;
import com.yiyan.careeryiyan.service.UserService;
import com.yiyan.careeryiyan.service.WebSocketDbAccessor;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/chat")
@CrossOrigin(origins ="*")
public class ChatController {
    @Autowired
    private MyWebSocket myWebSocket;
    @Resource
    private UserService userService;
    @Resource
    private WebSocketDbAccessor dbAccessor;
    @Autowired
    private ChatService chatService;

    @PostMapping("/testSend")
    public ResponseEntity testSend(@RequestBody Map<String, Object> map){
        String userId = (String)map.get("userId");
        String chatId = (String)map.get("chatId");
        String content = (String)map.get("content");
        System.out.println("userId:"+userId+" chatId:"+chatId+" content:"+content);
        myWebSocket.sendMsgToChat(chatId,content);
        System.out.println(userId + "send success");
        return ResponseEntity.ok(map);
    }

    @PostMapping("/fetch")
    public ResponseEntity fetchMessage(@RequestBody FetchMessagesRequest fetchMessagesRequest,
                                                 HttpServletRequest request){
        User user = (User)request.getAttribute("user");
        String chatId = fetchMessagesRequest.getChatId();
        int msgPage = fetchMessagesRequest.getMsgPage();
        int numMsgInPage = fetchMessagesRequest.getNumMsgInPage();
        Chat chat = chatService.getChatByChatId(chatId);
        if(chat==null|| !chat.checkUserInChat(user.getUserId())){
            throw new BaseException("聊天不存在或你不在此聊天中");
        }
        List<Message> messageListByChatId= chatService.getMessageListByChatId(chatId,msgPage, numMsgInPage );
        List<Map> messages = new ArrayList<>();
        for(Message message:messageListByChatId){

            Message repliedMessage = chatService.getMessageByMessageId(message.getMsgReplyMsgId());
            List<MessageFile> messageFileList = chatService.getMessageFileListByMsgId(message.getMsgId());
            Map<String,Object> map = new HashMap<>();
            map.put("msg",message);
            map.put("replyMsg",repliedMessage);
            map.put("files",messageFileList);

            messages.add(map);
        }
        int totalMsgNum = chatService.getMessageListByChatId(chatId,1, Integer.MAX_VALUE).size();
        boolean noMoreMsg = totalMsgNum <= msgPage * numMsgInPage;
        Map<String, Object> rsp = new HashMap<>();
        rsp.put("noMoreMsg",noMoreMsg);
        rsp.put("messages",messages);
        return ResponseEntity.ok(rsp);
    }
}
