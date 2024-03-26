package top.team7.chatroom.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import top.team7.chatroom.entity.GroupChatMessage;

import java.util.List;

@Repository
public interface GroupChatMessageRepository extends JpaRepository<GroupChatMessage, Long> {


    List<GroupChatMessage> findByGroupChatRoomGroupIdOrderBySendTimeAsc(Long groupId);


    List<GroupChatMessage> findByGroupChatRoomGroupIdAndSenderUserUsernameOrderBySendTimeAsc(Long groupId, String senderUsername);


    List<GroupChatMessage> findByGroupChatRoomGroupIdAndMessageTypeIdOrderBySendTimeAsc(Long groupId, int messageTypeId);
}
