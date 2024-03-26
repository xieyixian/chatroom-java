package top.javahai.chatroom.service;

import top.javahai.chatroom.entity.GroupChatMessage;
import top.javahai.chatroom.entity.GroupChatRoom;
import top.javahai.chatroom.entity.User;

import java.util.List;
import java.util.Optional;

public interface GroupChatService {


    void sendMessage(Long groupId, String senderUsername, String messageText, int messageTypeId);


    List<GroupChatMessage> getAllMessages(Long groupId);


    List<GroupChatMessage> getMessagesBySender(Long groupId, String senderUsername);


    List<GroupChatMessage> getMessagesByType(Long groupId, int messageTypeId);

    List<GroupChatRoom> getJoinedGroupChats(User currentUser);

    GroupChatRoom joinGroupChat(String groupName, Optional<User> owner, List<User> members);
}
