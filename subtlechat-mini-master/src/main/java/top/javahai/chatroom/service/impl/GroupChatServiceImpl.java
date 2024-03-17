package top.javahai.chatroom.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.javahai.chatroom.entity.GroupChatMessage;
import top.javahai.chatroom.entity.GroupChatRoom;
import top.javahai.chatroom.entity.GroupUser;
import top.javahai.chatroom.entity.User;
import top.javahai.chatroom.repository.GroupChatMessageRepository;
import top.javahai.chatroom.repository.GroupChatRoomRepository;
import top.javahai.chatroom.repository.GroupUserRepository;
import top.javahai.chatroom.repository.UserRepository;
import top.javahai.chatroom.service.GroupChatService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class GroupChatServiceImpl implements GroupChatService {

    private final GroupChatMessageRepository groupChatMessageRepository;
    private final GroupChatRoomRepository groupChatRoomRepository;
    private final UserRepository userRepository;
    private final GroupUserRepository groupUserRepository;

    @Autowired
    public GroupChatServiceImpl(GroupChatMessageRepository groupChatMessageRepository,
                                GroupChatRoomRepository groupChatRoomRepository,
                                UserRepository userRepository,
                                GroupUserRepository groupUserRepository) {
        this.groupChatMessageRepository = groupChatMessageRepository;
        this.groupChatRoomRepository = groupChatRoomRepository;
        this.userRepository = userRepository;
        this.groupUserRepository = groupUserRepository;
    }

    @Override
    public void sendMessage(Long groupId, String senderUsername, String messageText, int messageTypeId) {
        // 通过群组ID和发送者用户名从数据库中获取群组和发送者实体对象
        GroupChatRoom groupChatRoom = groupChatRoomRepository.findById(groupId).orElse(null);
        User senderUser = userRepository.findByUsername(senderUsername)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + senderUsername));

        if (groupChatRoom != null && senderUser != null) {
            GroupChatMessage groupChatMessage = new GroupChatMessage();
            groupChatMessage.setGroupChatRoom(groupChatRoom);
            groupChatMessage.setSenderUser(senderUser);
            groupChatMessage.setMessageText(messageText);
            groupChatMessage.setSendTime(LocalDateTime.now());
            groupChatMessage.setMessageTypeId(messageTypeId);
            groupChatMessageRepository.save(groupChatMessage);
        } else {
            // 处理未找到群组或发送者的情况
        }
    }

    @Override
    public List<GroupChatMessage> getAllMessages(Long groupId) {
        return groupChatMessageRepository.findByGroupChatRoomGroupIdOrderBySendTimeAsc(groupId);
    }

    @Override
    public List<GroupChatMessage> getMessagesBySender(Long groupId, String senderUsername) {
        return groupChatMessageRepository.findByGroupChatRoomGroupIdAndSenderUserUsernameOrderBySendTimeAsc(groupId, senderUsername);
    }

    @Override
    public List<GroupChatMessage> getMessagesByType(Long groupId, int messageTypeId) {
        return groupChatMessageRepository.findByGroupChatRoomGroupIdAndMessageTypeIdOrderBySendTimeAsc(groupId, messageTypeId);
    }

    @Override
    public List<GroupChatRoom> getJoinedGroupChats(User currentUser) {
        // 调用 repository 方法获取已加入的群聊列表
        return groupChatRoomRepository.findByGroupUsers_User(currentUser);
    }

    @Override
    public GroupChatRoom joinGroupChat(String groupName, Optional<User> owner, List<User> members) {
        // 创建群组
        GroupChatRoom groupChatRoom = new GroupChatRoom();
        groupChatRoom.setGroupName(groupName);

        // 设置群主
        User groupOwner = owner.orElse(null);
        if (groupOwner == null) {
            throw new RuntimeException("Owner user not found with username: " + owner);
        }
        groupChatRoom.setCreatorUsername(groupOwner.getUsername());

        // 保存群组信息到数据库
        groupChatRoom = groupChatRoomRepository.save(groupChatRoom);

        // 创建群组成员关联
        // 添加群主到群组成员中
        GroupUser ownerGroupUser = new GroupUser();
        ownerGroupUser.setUser(groupOwner);
        ownerGroupUser.setGroupChatRoom(groupChatRoom);
        groupUserRepository.save(ownerGroupUser);

        // 遍历members列表，逐个添加到群组成员中
        for (User member : members) {
            GroupUser memberGroupUser = new GroupUser();
            memberGroupUser.setUser(member);
            memberGroupUser.setGroupChatRoom(groupChatRoom);
            groupUserRepository.save(memberGroupUser);
        }

        // 返回创建的群组信息
        return groupChatRoom;
    }


}
