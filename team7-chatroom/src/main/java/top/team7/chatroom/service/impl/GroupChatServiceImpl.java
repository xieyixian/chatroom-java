package top.team7.chatroom.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.team7.chatroom.entity.GroupChatMessage;
import top.team7.chatroom.entity.GroupChatRoom;
import top.team7.chatroom.entity.GroupUser;
import top.team7.chatroom.entity.User;
import top.team7.chatroom.repository.GroupChatMessageRepository;
import top.team7.chatroom.repository.GroupChatRoomRepository;
import top.team7.chatroom.repository.GroupUserRepository;
import top.team7.chatroom.repository.UserRepository;
import top.team7.chatroom.service.GroupChatService;

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

        return groupChatRoomRepository.findByGroupUsers_User(currentUser);
    }

    @Override
    public GroupChatRoom joinGroupChat(String groupName, Optional<User> owner, List<User> members) {

        GroupChatRoom groupChatRoom = new GroupChatRoom();
        groupChatRoom.setGroupName(groupName);


        User groupOwner = owner.orElse(null);
        if (groupOwner == null) {
            throw new RuntimeException("Owner user not found with username: " + owner);
        }
        groupChatRoom.setCreatorUsername(groupOwner.getUsername());


        groupChatRoom = groupChatRoomRepository.save(groupChatRoom);

        //Create group member association
        //Add the group owner to the group members
        GroupUser ownerGroupUser = new GroupUser();
        ownerGroupUser.setUser(groupOwner);
        ownerGroupUser.setGroupChatRoom(groupChatRoom);
        groupUserRepository.save(ownerGroupUser);

        // Traverse the members list and add them to the group members one by one
        for (User member : members) {
            GroupUser memberGroupUser = new GroupUser();
            memberGroupUser.setUser(member);
            memberGroupUser.setGroupChatRoom(groupChatRoom);
            groupUserRepository.save(memberGroupUser);
        }

        // Return the created group information
        return groupChatRoom;
    }


}
