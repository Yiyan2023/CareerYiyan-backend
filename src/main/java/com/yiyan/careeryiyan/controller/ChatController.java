package com.yiyan.careeryiyan.controller;

import com.yiyan.careeryiyan.model.request.SendRequest;
import com.yiyan.careeryiyan.exception.BaseException;
import com.yiyan.careeryiyan.model.domain.*;
import com.yiyan.careeryiyan.model.request.FetchMessagesRequest;
import com.yiyan.careeryiyan.model.response.StringResponse;
import com.yiyan.careeryiyan.service.ChatService;
import com.yiyan.careeryiyan.service.MyWebSocket;
import com.yiyan.careeryiyan.service.UserService;
import com.yiyan.careeryiyan.service.WebSocketDbAccessor;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
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

    @PostMapping("/getAllChat")
    public ResponseEntity getAllChat(HttpServletRequest request){
        User user = (User)request.getAttribute("user");
        List<Chat> chatList = chatService.getChatListByUserId(user.getUserId());
        List<Map> chats = new ArrayList<>();
        for(Chat chat:chatList){
            Map<String,Object> map = new HashMap<>();
            map.put("chat",chat);
            map.put("lastMessage",chatService.getLastMessageInChat(chat.getChatId()));
            int unreadCount = chatService.getUnreadCount(chat.getChatId(),user.getUserId());
            map.put("unreadCount", unreadCount);
            UserOnline userOnline = userService.getUserOnline(chat.getAnotherUserId(user.getUserId()));
            if(userOnline == null){
                userOnline = new UserOnline();
                userOnline.setUserOnlineStatus("offline");
            }
            map.put("userStatus",userOnline);
            //map.put("members",dbAccessor.getTeamMembersById(chat.getTeamId()));
            chats.add(map);
        }
        return ResponseEntity.ok(chats);
    }
    @PostMapping("/send")
    public ResponseEntity send(@RequestBody SendRequest sendRequest, HttpServletRequest request){
        User user = (User)request.getAttribute("user");
        Message message = sendRequest.getMessage();
        List<MessageFile> messageFiles = sendRequest.getFiles();
        message.setMsgCreateAt(LocalDateTime.now());
        chatService.addMessage(message);
        for(MessageFile massageFile:messageFiles){
            massageFile.setMsgFileMsgId(message.getMsgId());
            chatService.addMessageFile(massageFile);
        }
        return ResponseEntity.ok(new StringResponse(message.getMsgId()));
    }
}
