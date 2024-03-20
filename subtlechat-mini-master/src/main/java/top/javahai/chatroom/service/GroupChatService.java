package top.javahai.chatroom.service;

import top.javahai.chatroom.entity.GroupChatMessage;
import top.javahai.chatroom.entity.GroupChatRoom;
import top.javahai.chatroom.entity.User;

import java.util.List;
import java.util.Optional;

public interface GroupChatService {

    // 发送群聊消息
    void sendMessage(Long groupId, String senderUsername, String messageText, int messageTypeId);

    // 获取群组中的所有消息
    List<GroupChatMessage> getAllMessages(Long groupId);

    // 获取群组中指定发送者的所有消息
    List<GroupChatMessage> getMessagesBySender(Long groupId, String senderUsername);

    // 获取群组中指定类型的所有消息
    List<GroupChatMessage> getMessagesByType(Long groupId, int messageTypeId);

    List<GroupChatRoom> getJoinedGroupChats(User currentUser);

    GroupChatRoom joinGroupChat(String groupName, Optional<User> owner, List<User> members);
}
