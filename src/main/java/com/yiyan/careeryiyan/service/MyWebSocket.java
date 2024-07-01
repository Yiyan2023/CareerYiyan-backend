package com.yiyan.careeryiyan.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yiyan.careeryiyan.exception.BaseException;
import com.yiyan.careeryiyan.model.MessageToFrontend;
import com.yiyan.careeryiyan.model.domain.*;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * 使用springboot的唯一区别是要@Component声明下，而使用独立容器是由容器自己管理websocket的，
 * 但在springboot中连容器都是spring管理的。
 * 虽然@Component默认是单例模式的，但springboot还是会为每个websocket连接初始化一个bean，
 * 所以可以用一个静态set保存起来。
 */
@ServerEndpoint(value = "/websocket/{nickname}")
@Component
public class MyWebSocket {

    private static WebSocketDbAccessor dbAccessor;
    private static ChatService chatService;
    private static UserService userService;
    private static NoticeService noticeService;

    @Autowired
    public void setDbAccessor(WebSocketDbAccessor dbAccessor, ChatService chatService, UserService userService) {
        MyWebSocket.dbAccessor = dbAccessor;
        MyWebSocket.chatService = chatService;
        MyWebSocket.userService = userService;
    }

    public static Map<String, Session> getMap() {
        return map;
    }

    public static void setMap(Map<String, Session> map) {
        MyWebSocket.map = map;
    }

    public static Map<String, String> getUser2sessionId() {
        return user2sessionId;
    }

    public static Map<String, List<String>> getUser2sessionIds() {
        return user2sessionIds;
    }


    public static void setUser2sessionId(Map<String, String> user2sessionId) {
        MyWebSocket.user2sessionId = user2sessionId;
    }

//    @Autowired
//    public void setDbAccessor(WebSocketDbAccessor dbAccessor) {
//        MyWebSocket.dbAccessor = dbAccessor;
//    }

    private static Map<String, Session> map = new HashMap<>();
    //username和sessionId绑定
    private static Map<String, String> user2sessionId = new HashMap<>();
    private static Map<String, List<String>> user2sessionIds = new HashMap<>();

    //用来存放每个客户端对应的MyWebSocket对象。
    private static CopyOnWriteArraySet<MyWebSocket> webSocketSet = new CopyOnWriteArraySet<>();
    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;

    public Session getSession() {
        return session;
    }

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("nickname") String userId) {
        this.session = session;
        if (userId.startsWith("chat")) {
            userId = userId.substring(4);
            System.out.println("userId = "+userId + "试图建立聊天连接");


            if (getUser2sessionId().containsKey(userId)) {
                for (MyWebSocket myWebSocket : webSocketSet) {
                    if (Objects.equals(myWebSocket.session.getId(), getUser2sessionId().get(userId))) {
                        try {
                            if (myWebSocket.session.isOpen()) {
                                System.out.println("userId:"+ userId+"再次连接, 关闭先前连接！！");
                                myWebSocket.session.close();
                                webSocketSet.remove(myWebSocket);
                                session.close();
                            }else{
                                //聊天连接存在但已关闭，删除
                                webSocketSet.remove(myWebSocket);
                            }
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }
                        break;
                    }
                }
            }
            getMap().put(session.getId(), session);
            webSocketSet.add(this);     //加入set中
            //删除原来的连接,建立新连接
            System.out.println("userId:"+userId+"聊天连接建立成功");

            getUser2sessionId().remove(userId);
            getUser2sessionId().put(userId, session.getId());

            System.out.print("有新连接加入 userId:" + userId + ",当前在线人数为" + webSocketSet.size());
            for (String key : getUser2sessionId().keySet()) {
                //System.out.println("nickname:"+key+"的频道:"+ getUser2sessionId().get(key));
                System.out.println("\n--userId:" + key + "的频道:");
                if (getUser2sessionId().get(key) != null) System.out.print(" " + getUser2sessionId().get(key));
                UserOnline userOnline = userService.getUserOnlineByUserId(userId);
                if(userOnline == null){
                    userOnline = new UserOnline();
                    userOnline.setUserOnlineUserId(userId);
                    userOnline.setUserOnlineStatus("online");
                    userOnline.setUserOnlineLastChangeAt(LocalDateTime.now());
                    userService.addUserOnline(userOnline);
                }
                else{
                    userOnline.setUserOnlineStatus("online");
                    userOnline.setUserOnlineLastChangeAt(LocalDateTime.now());
                    userService.updateUserOnline(userOnline);
                }
            }
            System.out.println("\n建立的连接总数:" + webSocketSet.size());
            return;
        }
//        webSocketSet.add(this);     //加入set中
//        getMap().put(session.getId(), session);
//
//        getUser2sessionIds().computeIfAbsent(userId, k -> new ArrayList<>());
//        getUser2sessionIds().get(userId).add(session.getId());
//
//        webSocketSet.add(this);     //加入set中
//        System.out.print("有新连接加入:" + userId + ",当前在线人数为" + webSocketSet.size());
//        for (String key : getUser2sessionId().keySet()) {
//            //System.out.println("nickname:"+key+"的频道:"+ getUser2sessionId().get(key));
//            System.out.print("\n--nickname:" + key + "的频道:");
//            if (getUser2sessionId().get(key) != null) System.out.print(" " + getUser2sessionId().get(key));
//            if (getUser2sessionIds().get(key) == null) continue;
//            for (String channelId : getUser2sessionIds().get(key)) {
//                System.out.print(" " + channelId);
//            }
//            System.out.println();
//        }
//        System.out.println("\n建立的连接总数:" + webSocketSet.size());

    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        webSocketSet.remove(this);  //从set中删除

        for (String key : getUser2sessionId().keySet()) {
            if (Objects.equals(getUser2sessionId().get(key), session.getId())) {
                System.out.println("key-"+key);
                UserOnline userOnline = userService.getUserOnlineByUserId(key);
                System.out.println("userId:" + key + " 断开连接！");
                if(userOnline != null) {
                    userOnline.setUserOnlineLastChangeAt(LocalDateTime.now());
                    userOnline.setUserOnlineStatus("offline");
                    userOnline.setUserOnlineChatId(null);
                    userService.updateUserOnline(userOnline);
                }
                if (getUser2sessionIds().get(key) != null)
                    getUser2sessionIds().get(key).remove(session.getId());
                break;
            }
        }

        System.out.println("有一连接关闭！当前在线人数为" + webSocketSet.size());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message,
                          Session session, @PathParam("nickname") String nickname) {
        System.out.println("来自客户端的消息-->" + nickname + ": " + message);
    }

    /**
     * 发生错误时调用
     */
    @OnError
    public void onError(Session session, Throwable error) {
        System.out.println("发生错误");
        error.printStackTrace();
    }

    /**
     * 群发自定义消息
     */

    public void sendMsgToChat(String chatId, String content) {
        Chat chat = chatService.getChatByChatId(chatId);
        if (chat == null) return;
        String channel = getUser2sessionId().get(String.valueOf(chat.getChatUserId1()));
        Session toSession = getMap().get(channel);
        if (toSession != null && toSession.isOpen()) {
            toSession.getAsyncRemote().sendText(content);
        }
        channel = getUser2sessionId().get(String.valueOf(chat.getChatUserId2()));
        toSession = getMap().get(channel);
        if (toSession != null && toSession.isOpen()) {
            toSession.getAsyncRemote().sendText(content);
        }

    }

    /**
     * 群发自定义消息
     */

    public void send2Chat(Message message, List<MessageFile> files) {
        System.out.println("send2Chat");
        ObjectMapper objectMapper= new ObjectMapper();
        Map<String, Object> rspMap = new HashMap<>();
        boolean isRead = false;
        String chatId = message.getMsgChatId();
        User sender = userService.getUserInfo(message.getMsgSendUserId());
        UserOnline senderOnline  = userService.getUserOnlineByUserId(sender.getUserId());
        System.out.println("chatId:" + chatId);
        Chat chat = chatService.getChatByChatId(chatId);
        if (chat == null) {
            System.out.println("你不能在空群聊中发送信息！");
            throw new BaseException("你不能在空群聊中发送信息");
            //return false;
        }
        User receiver = userService.getUserInfo(chat.getAnotherUserId(sender.getUserId()));
        UserOnline receiverOnline = userService.getUserOnlineByUserId(receiver.getUserId());
        message.setMsgIsRead(0);
        if (receiverOnline == null || Objects.equals(receiverOnline.getUserOnlineStatus(), "offline")) {
            message.setMsgIsRead(0);
            System.out.println("对方不在线");
        }
        else {
            if (receiverOnline.getUserOnlineChatId() != null && receiverOnline.getUserOnlineChatId().equals(chatId)) {
                message.setMsgIsRead(1);
                System.out.println("1");
            } else {
                message.setMsgIsRead(0);
                System.out.println("2");
            }
            chatService.setMessageIsRead(message);
            String channel = getUser2sessionId().get(receiver.getUserId());
            Session toSession = getMap().get(channel);
            if (toSession != null && toSession.isOpen()) {
                System.out.println("3");
                int unreadCount  = chatService.getUnreadCount(chatId,receiver.getUserId());
                System.out.println("send2Chat: "+unreadCount);
                rspMap.put("chat",chat);
                rspMap.put("user", sender);
                rspMap.put("message",message);
                rspMap.put("userStatus",senderOnline);
                rspMap.put("unReadCount",unreadCount);
                rspMap.put("files", files);
                rspMap.put("curUserChatId", receiverOnline.getUserOnlineChatId());

                try {
                    MessageToFrontend msg = new MessageToFrontend<>(rspMap, 1);
                    String json = objectMapper.writeValueAsString(msg);

                    toSession.getAsyncRemote().sendText(json);

                    sendNotice(receiver.getUserId());
                }
                catch (Exception e){
                    throw new BaseException("发送失败");
                }
            }
            else {
                System.out.println("4");
                message.setMsgIsRead(0);
            }
        }
    }

    public void sendNotice(String userId) {
        ObjectMapper objectMapper= new ObjectMapper();
        Map<String, Object> rspMap = new HashMap<>();
        String channel = getUser2sessionId().get(userId);
        Session toSession = getMap().get(channel);
//        type = 2 推送新的未读通知数量
//
//        0：系统通知：纯文本通知
//        1：点赞我的：跳转到所属动态，评论点赞也跳到动态，post_id
//        2：评论我的：跳转到所属动态,post_id
//        3：招聘通知：推送新岗位（跳转到企业的招聘列表），招聘情况（跳转到已申请列表），ep_id
//        4：动态通知
//        5：未读私信总数
        if (toSession != null && toSession.isOpen()) {
            try {
                int num0 = 0, num1 = 0, num2 = 0, num3 = 0, num4 = 0, num5 = 0;
                List<Notice> allNotice = noticeService.getNotices(userId);
                for(Notice n: allNotice){
                    if(Objects.equals(n.getNoticeType(), "0")) num0++;
                    if(Objects.equals(n.getNoticeType(), "1")) num1++;
                    if(Objects.equals(n.getNoticeType(), "2")) num2++;
                    if(Objects.equals(n.getNoticeType(), "3")) num3++;
                    if(Objects.equals(n.getNoticeType(), "4")) num4++;
                }
                num5 = chatService.getTotalUnreadCount(userId);

                rspMap.put("num0", num0);
                rspMap.put("num1", num1);
                rspMap.put("num2", num2);
                rspMap.put("num3", num3);
                rspMap.put("num4", num4);
                rspMap.put("num5", num5);
                MessageToFrontend msg = new MessageToFrontend<>(rspMap, 2);
                String json = objectMapper.writeValueAsString(msg);
                toSession.getAsyncRemote().sendText(json);
            }
            catch (Exception e){
                throw new BaseException("推送通知数量失败");
            }
        }
    }

    @Autowired
    public void setNoticeService(NoticeService noticeService) {
        this.noticeService = noticeService;
    }

    public void tellYouIReadYourMessage(String chatId, String youId) {
        ObjectMapper objectMapper= new ObjectMapper();
        UserOnline youOnline = userService.getUserOnlineByUserId(youId);
        if(youOnline == null || Objects.equals(youOnline.getUserOnlineStatus(), "offline")){
            return;
        }
        String channel = getUser2sessionId().get(youId);
        Session toSession = getMap().get(channel);
        if (toSession != null && toSession.isOpen()) {
            Map<String,String> chatIdMap = new HashMap<>();
            chatIdMap.put("chatId", chatId);
            MessageToFrontend msg = new MessageToFrontend<>(chatIdMap, 3);
            String json = null;
            try {
                json = objectMapper.writeValueAsString(msg);
                toSession.getAsyncRemote().sendText(json);
            } catch (JsonProcessingException e) {
                throw new BaseException("告知对方已读失败");
            }


        }
    }
}