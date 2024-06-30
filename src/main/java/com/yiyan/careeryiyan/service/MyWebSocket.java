package com.yiyan.careeryiyan.service;

import com.yiyan.careeryiyan.model.domain.Chat;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * 使用springboot的唯一区别是要@Component声明下，而使用独立容器是由容器自己管理websocket的，
 * 但在springboot中连容器都是spring管理的。
 虽然@Component默认是单例模式的，但springboot还是会为每个websocket连接初始化一个bean，
 所以可以用一个静态set保存起来。
 */
@ServerEndpoint(value = "/websocket/{nickname}")
@Component
public class MyWebSocket {

    private static WebSocketDbAccessor dbAccessor;
    private static ChatService chatService;
    @Autowired
    public void setDbAccessor(WebSocketDbAccessor dbAccessor,ChatService chatService) {
        MyWebSocket.dbAccessor = dbAccessor;
        MyWebSocket.chatService = chatService;
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
    public void onOpen(Session session, @PathParam("nickname") String nickname) {
        this.session = session;
        if (nickname.startsWith("chat")) {
            System.out.println(nickname + "试图建立聊天连接");
            nickname = nickname.substring(4);

            if (getUser2sessionId().containsKey(nickname)) {
                for (MyWebSocket myWebSocket : webSocketSet) {
                    if (Objects.equals(myWebSocket.session.getId(), getUser2sessionId().get(nickname))) {
                        try {
                            if (myWebSocket.session.isOpen()) {
                                System.out.println("聊天连接已存在，禁止再次连接！");
                                session.close();
                                return;
                            }
                            //聊天连接存在但已关闭，删除
                            webSocketSet.remove(myWebSocket);
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
            System.out.println("聊天连接建立成功");

            getUser2sessionId().remove(nickname);
            getUser2sessionId().put(nickname, session.getId());

            System.out.print("有新连接加入:" + nickname + ",当前在线人数为" + webSocketSet.size());
            for (String key : getUser2sessionId().keySet()) {
                //System.out.println("nickname:"+key+"的频道:"+ getUser2sessionId().get(key));
                System.out.print("\n--nickname:" + key + "的频道:");
                if (getUser2sessionId().get(key) != null) System.out.print(" " + getUser2sessionId().get(key));
                if (getUser2sessionIds().get(key) == null) continue;
                for (String channelId : getUser2sessionIds().get(key)) {
                    System.out.print(" " + channelId);
                }
                System.out.println();
            }
            System.out.println("\n建立的连接总数:" + webSocketSet.size());
            return;
        }
        webSocketSet.add(this);     //加入set中
        getMap().put(session.getId(), session);

        getUser2sessionIds().computeIfAbsent(nickname, k -> new ArrayList<>());
        getUser2sessionIds().get(nickname).add(session.getId());

        webSocketSet.add(this);     //加入set中
        System.out.print("有新连接加入:" + nickname + ",当前在线人数为" + webSocketSet.size());
        for (String key : getUser2sessionId().keySet()) {
            //System.out.println("nickname:"+key+"的频道:"+ getUser2sessionId().get(key));
            System.out.print("\n--nickname:" + key + "的频道:");
            if (getUser2sessionId().get(key) != null) System.out.print(" " + getUser2sessionId().get(key));
            if (getUser2sessionIds().get(key) == null) continue;
            for (String channelId : getUser2sessionIds().get(key)) {
                System.out.print(" " + channelId);
            }
            System.out.println();
        }
        System.out.println("\n建立的连接总数:" + webSocketSet.size());

    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        webSocketSet.remove(this);  //从set中删除
        for (String key : getUser2sessionId().keySet()) {
            if (Objects.equals(getUser2sessionId().get(key), session.getId())) {
                getUser2sessionId().remove(key);
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

    public void sendMsgToChat(String chatId,String content){
        Chat chat = chatService.getChatByChatId(chatId);
        if(chat==null)return;
        String channel = getUser2sessionId().get(String.valueOf(chat.getChatUserId1()));
        Session toSession = getMap().get(channel);
        if(toSession!=null&&toSession.isOpen()){
            toSession.getAsyncRemote().sendText(content);
        }
        channel = getUser2sessionId().get(String.valueOf(chat.getChatUserId2()));
        toSession = getMap().get(channel);
        if(toSession!=null&&toSession.isOpen()){
            toSession.getAsyncRemote().sendText(content);
        }

    }

//    public boolean broadcast(SocketMsg socketMsg, MessageDetail messageDetail) {
//
//        boolean isRead = false;
//        String roomId = socketMsg.getRoomId();
//        UserOnline sender = dbAccessor.getUserOnlineById(socketMsg.getSenderId());
//        System.out.println("roomId:" + roomId);
//        List<RoomUser> roomMembers = dbAccessor.getRoomMembersById(Integer.parseInt(roomId));
//        if (roomMembers == null) {
//            System.out.println("你不能在空群聊中发送信息！");
//            return false;
//        }
//        System.out.println("id为" + roomId + "的聊天室有" + roomMembers.size() + "个成员");
//        Room room = dbAccessor.getRoomByRoomId(Integer.parseInt(roomId));
//        if (room.getType() != 1) isRead = true;
//
//        RoomDetail roomDetail = new RoomDetail(room);
//        if(room.getType()==1) {
//            roomDetail.setRoomName(sender.getName());
//            roomDetail.setAvatar(sender.getProfilePhotoUrl());
//            System.out.println(sender.getId());
//            System.out.println("私聊，roomName:"+roomDetail.getRoomName()+" avatar:"+roomDetail.getAvatar());
//        }
//        roomDetail.setLastMessage(messageDetail);
//        for (RoomUser userSummary : roomMembers) {
//            User user = dbAccessor.getUserById(userSummary.getUserId());
//            UserInRoom userInRoom = new UserInRoom(user, dbAccessor.getIdentityInRoom(room.getId(), user.getId()));
//            roomDetail.getUsers().add(userInRoom);
//        }
//
//        for (RoomUser userSummary : roomMembers) {
//            //不给自己发
//
//            String receiverId = String.valueOf(userSummary.getUserId());
//            if (receiverId.equals(String.valueOf(sender.getId()))) continue;
//            //得到接收者的频道号
//            String receiverChannelId = getUser2sessionId().get(receiverId);
//            boolean flag = false;
//            //System.out.println("receiverId:" + receiverId + " receiverChannelId:" + receiverChannelId);
//            //不管在不在聊天室，都设置成可见
//            dbAccessor.setRoomDisplay(Integer.parseInt(socketMsg.getRoomId()), Integer.parseInt(receiverId));
//
//            for (MyWebSocket webSocket : webSocketSet) {
//                try {
//                    // System.out.println("webSocket.session.getId():" + webSocket.session.getId());
//                    ObjectMapper objectMapper = new ObjectMapper();
//                    String json;
//                    //接收者在线，但在不在当前团队不一定
//                    //if(receiverChannelIds.contains(webSocket.session.getId()))
//                    if (Objects.equals(webSocket.session.getId(), receiverChannelId)) {
//                        UserOnline receiver = dbAccessor.getUserOnlineById(receiverId);
//
//                        System.out.print("receiverId:" + receiverId + " currentTeam:" + receiver.getCurrentTeamId());
//                        System.out.println(" senderId:" + socketMsg.getSenderId() + " currentRoom:" + roomId);
//                        //接收者不与发送者选择同一团队，即使在线也不给他发
//                        if (receiver.getCurrentTeamId() != room.getTeamId()) {
//                            System.out.println("----此接收者在线但不在同一团队，不给他发");
//                            break;
//                        }
//
//                        //此时一定在同一团队
//                        flag = true;
//
//                        if (Objects.equals(receiver.getCurrentRoomId(), socketMsg.getRoomId())) {
//                            //在当前聊天室中
//                            messageDetail.addSender(sender);
//                            MessageToFrontend<MessageDetail> messageToFrontend = new MessageToFrontend<>(messageDetail, 1);
//                            json = objectMapper.writeValueAsString(messageToFrontend);
//                            System.out.println("发出去的json：" + json);
//                            webSocket.session.getAsyncRemote().sendText(json);
//                            isRead = true;
//                        } else {
//                            //用户不在当前聊天室中，//应该发送一个roomDetail
//
//                            roomDetail.getUsers().sort(RoomDetail.userComparator);
//                            //增加未读消息数
//                            dbAccessor.addUnreadCountInRoom(room.getId(), receiver.getId());
//                            int unreadCount = dbAccessor.getUnreadMsgCountInRoom(room.getId(), receiver.getId());
//                            roomDetail.setUnreadCount(String.valueOf(unreadCount));
//                            try {
//                                MessageToFrontend<RoomDetail> messageToFrontend = new MessageToFrontend<>(roomDetail, 2);
//                                json = objectMapper.writeValueAsString(messageToFrontend);
//                                webSocket.session.getAsyncRemote().sendText(json);
//                                System.out.println("发出去的json：" + json);
//                            } catch (JsonProcessingException e) {
//                                throw new RuntimeException(e);
//                            }
//                        }
//                        break;//给这个人发送完毕，退出循环，再看下一个人
//                    }
//                } catch (Exception e) {
//                    System.out.println(e.getMessage());
//                }
//            }
//            if (!flag) {
//                //用户不在线或未选择发送者的团队，增加未读消息数
//                dbAccessor.addUnreadCountInRoom(room.getId(), userSummary.getUserId());
//            }
//        }
//        return isRead;
//    }

//    public void sendChatNoticeToUser(SocketMsgChat socketMsgChat) {
//        int senderId = socketMsgChat.getSenderId();
//        int receiverId = socketMsgChat.getReceiverId();
//        int roomId = socketMsgChat.getRoomId();
//        int messageId = socketMsgChat.getMessageId();
//
//        List<String> receiverChannelIds = getUser2sessionIds().get(String.valueOf(receiverId));
//
//        UserOnline sender = dbAccessor.getUserOnlineById(String.valueOf(senderId));
//        UserOnline receiver = dbAccessor.getUserOnlineById(String.valueOf(receiverId));
//        ObjectMapper objectMapper = new ObjectMapper();
//        System.out.println("sendNoticeToUserId:" + receiverId);
//        //接收者在线且与发送者选择同一团队
//        for (MyWebSocket webSocket : webSocketSet) {
//            if (receiverChannelIds == null) {
//                //用户不在线
//                break;
//            }
//            if (receiverChannelIds.contains(webSocket.session.getId()) && webSocket.session.isOpen()) {
//                Session toSession = webSocket.session;
//                if (receiver != null && toSession != null
//                        && toSession.isOpen()
//                        && receiver.getCurrentTeamId() == sender.getCurrentTeamId()) {
//                    ChatAtNoticeDetail chatAtNoticeDetail = getChatAtNoticeDetail(senderId, roomId, messageId, sender);
//                    try {
//                        MessageToFrontend<ChatAtNoticeDetail> messageToFrontend = new MessageToFrontend<>(chatAtNoticeDetail, 4);
//                        String json = objectMapper.writeValueAsString(messageToFrontend);
//                        toSession.getAsyncRemote().sendText(json);
//                        System.out.println("发出去的json：" + json);
//
//                        int teamId = sender.getCurrentTeamId();
//                        int unreadChatNoticeCount = dbAccessor.getUnreadChatNoticeCount(teamId, receiverId);
//                        int unreadFileNoticeCount = dbAccessor.getUnreadFileNoticeCount(teamId, receiverId);
//                        MessageToFrontend<Integer> messageToFrontend2 = new MessageToFrontend<>(unreadChatNoticeCount + unreadFileNoticeCount + 1, 6);
//                        json = objectMapper.writeValueAsString(messageToFrontend2);
//                        webSocket.session.getAsyncRemote().sendText(json);
//                        System.out.println("发出去的json：" + json);
//
//                    } catch (Exception e) {
//                        System.out.println(e.getMessage());
//                    }
//                }
//            }
//        }
//        //不管在不在线和在不在团队，都给他通知存下来
//        ChatNoticeHistory chatNoticeHistory = new ChatNoticeHistory();
//
//        chatNoticeHistory.setRoomId(roomId);
//        chatNoticeHistory.setChatId(messageId);
//        chatNoticeHistory.setTeamId(sender.getCurrentTeamId());
//        chatNoticeHistory.setUserId(receiverId);
//        Room room = dbAccessor.getRoomByRoomId(roomId);
//        if(room.getType()==1)chatNoticeHistory.setChatName(sender.getName());
//        else
//            chatNoticeHistory.setChatName(room.getName());
//
//        dbAccessor.addChatNoticeHistory(chatNoticeHistory);
//
//    }

//    private ChatAtNoticeDetail getChatAtNoticeDetail(int senderId, int roomId, int messageId, UserOnline sender) {
//        System.out.println("被艾特者在线且与艾特者处于同一团队");
//        ChatAtNoticeDetail chatAtNoticeDetail = new ChatAtNoticeDetail();
//        chatAtNoticeDetail.setUserId(String.valueOf(senderId));
//        chatAtNoticeDetail.setUsername(sender.getName());
//        chatAtNoticeDetail.setAvatar(sender.getProfilePhotoUrl());
//
//        ChatHistory chatHistory = dbAccessor.getChatHistoryById(messageId);
//        chatAtNoticeDetail.setContent(chatHistory.getContent());
//
//        Room room = dbAccessor.getRoomByRoomId(roomId);
//        chatAtNoticeDetail.setRoomId(String.valueOf(roomId));
//        if (room.getType() == 1) chatAtNoticeDetail.setRoomName(sender.getName());
//        else
//            chatAtNoticeDetail.setRoomName(room.getName());
//        return chatAtNoticeDetail;
//    }

//    public void sendChatNoticeToRoom(SocketMsgChat socketMsgChat) {
//        int senderId = socketMsgChat.getSenderId();
//
//        int roomId = socketMsgChat.getRoomId();
//        int messageId = socketMsgChat.getMessageId();
//
//        UserOnline sender = dbAccessor.getUserOnlineById(String.valueOf(senderId));
//        int teamId = sender.getCurrentTeamId();
//
//        List<RoomUser> roomMembers = dbAccessor.getRoomMembersById(roomId);
//        if (roomMembers != null)
//            for (RoomUser user : roomMembers) {
//                System.out.println("roomMember's id:" + user.getUserId());
//            }
//        try {
//            ObjectMapper objectMapper = new ObjectMapper();
//            String json;
//            if (roomMembers != null) {
//                for (RoomUser user : roomMembers) {
//                    String receiverId = String.valueOf(user.getUserId());
//                    if (receiverId.equals(String.valueOf(socketMsgChat.getSenderId()))) continue;
//                    String receiverChannelId = getUser2sessionId().get(receiverId);
//                    List<String> receiverChannelIds = getUser2sessionIds().get(receiverId);
//
//                    for (MyWebSocket webSocket : webSocketSet) {
//                        if (receiverChannelIds == null) {
//                            //用户不在线
//                            break;
//                        }
//                        //接收者在线，但在不在当前团队不一定
//                        if (receiverChannelIds.contains(webSocket.session.getId()) && webSocket.session.isOpen()) {
//                            System.out.print("receiverId:" + receiverId + " receiverChannelId:" + receiverChannelId);
//                            UserOnline receiver = dbAccessor.getUserOnlineById(receiverId);
//                            System.out.print(" currentTeam:" + receiver.getCurrentTeamId());
//                            System.out.println("  senderId:" + socketMsgChat.getSenderId() + " currentTeam:" + teamId);
//                            //接收者不与发送者选择同一团队，即使在线也不给他发
//                            if (receiver.getCurrentTeamId() != teamId) {
//                                break;
//                            }
//                            //此时一定在同一团队
//                            ChatAtNoticeDetail chatAtNoticeDetail = getChatAtNoticeDetail(senderId, roomId, messageId, sender);
//                            try {
//                                MessageToFrontend<ChatAtNoticeDetail> messageToFrontend = new MessageToFrontend<>(chatAtNoticeDetail, 4);
//                                json = objectMapper.writeValueAsString(messageToFrontend);
//                                webSocket.session.getAsyncRemote().sendText(json);
//                                System.out.println("发出去的json：" + json);
//
//                                int unreadChatNoticeCount = dbAccessor.getUnreadChatNoticeCount(teamId, Integer.parseInt(receiverId));
//                                int unreadFileNoticeCount = dbAccessor.getUnreadFileNoticeCount(teamId, Integer.parseInt(receiverId));
//                                MessageToFrontend<Integer> messageToFrontend2 = new MessageToFrontend<>(unreadChatNoticeCount + unreadFileNoticeCount + 1, 6);
//                                json = objectMapper.writeValueAsString(messageToFrontend2);
//                                webSocket.session.getAsyncRemote().sendText(json);
//                                System.out.println("发出去的json：" + json);
//                            } catch (Exception e) {
//                                System.out.println(e.getMessage());
//                            }
//
//                        }
//                    }
//
//                    //不管在不在线在不在同一团队，都给他通知存下来
//                    ChatNoticeHistory chatNoticeHistory = new ChatNoticeHistory();
//
//                    chatNoticeHistory.setChatId(messageId);
//                    chatNoticeHistory.setTeamId(teamId);
//                    chatNoticeHistory.setRoomId(roomId);
//                    chatNoticeHistory.setUserId(Integer.parseInt(receiverId));
//                    Room room = dbAccessor.getRoomByRoomId(roomId);
//                    if(room.getType()==1)chatNoticeHistory.setChatName(sender.getName());
//                    else
//                        chatNoticeHistory.setChatName(room.getName());
//
//                    dbAccessor.addChatNoticeHistory(chatNoticeHistory);
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }


//    public void tellMeHowManyUnreadNotice(int userId){
//        List<String> receiverChannelIds = getUser2sessionIds().get(String.valueOf(userId));
//        if(receiverChannelIds==null)return;
//        UserOnline receiver = dbAccessor.getUserOnlineById(String.valueOf(userId));
//        if(receiver==null)return;
//        int teamId = receiver.getCurrentTeamId();
//        int unreadChatNoticeCount = dbAccessor.getUnreadChatNoticeCount(teamId, userId);
//        int unreadFileNoticeCount = dbAccessor.getUnreadFileNoticeCount(teamId, userId);
//        ObjectMapper objectMapper = new ObjectMapper();
//        try {
//            MessageToFrontend<Integer> messageToFrontend = new MessageToFrontend<>(unreadChatNoticeCount + unreadFileNoticeCount, 6);
//            String json = objectMapper.writeValueAsString(messageToFrontend);
//            for (MyWebSocket webSocket : webSocketSet) {
//                if (receiverChannelIds.contains(webSocket.session.getId()) && webSocket.session.isOpen()) {
//                    webSocket.session.getAsyncRemote().sendText(json);
//                    System.out.println("发出去的json：" + json);
//                }
//            }
//        } catch (JsonProcessingException e) {
//            throw new RuntimeException(e);
//        }
//
//    }
//
//    public void tellUserIRead(int roomId, int receiverId) {
//        MessageToFrontend<String> messageToFrontend = new MessageToFrontend<>("P" + roomId, 7);
//        UserOnline receiver = dbAccessor.getUserOnlineById(String.valueOf(receiverId));
//        if (receiver == null) return;
//        String receiverChannelId = getUser2sessionId().get(String.valueOf(receiverId));
//        if (receiverChannelId == null) return;
//        Session toSession = getMap().get(receiverChannelId);
//        if (toSession == null || !toSession.isOpen()) return;
//        ObjectMapper objectMapper = new ObjectMapper();
//        try {
//            String json = objectMapper.writeValueAsString(messageToFrontend);
//            toSession.getAsyncRemote().sendText(json);
//            System.out.println("发出去的json：" + json);
//        } catch (JsonProcessingException e) {
//            throw new RuntimeException(e);
//        }
//    }


}