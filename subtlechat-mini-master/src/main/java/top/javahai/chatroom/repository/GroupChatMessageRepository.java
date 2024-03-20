package top.javahai.chatroom.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import top.javahai.chatroom.entity.GroupChatMessage;

import java.util.List;

@Repository
public interface GroupChatMessageRepository extends JpaRepository<GroupChatMessage, Long> {

    // 根据群组ID查询该群组的所有消息，按发送时间升序排序
    List<GroupChatMessage> findByGroupChatRoomGroupIdOrderBySendTimeAsc(Long groupId);

    // 根据群组ID和发送者用户名查询该群组中指定发送者发送的所有消息，按发送时间升序排序
    List<GroupChatMessage> findByGroupChatRoomGroupIdAndSenderUserUsernameOrderBySendTimeAsc(Long groupId, String senderUsername);

    // 根据群组ID和消息类型ID查询该群组中指定类型的所有消息，按发送时间升序排序
    List<GroupChatMessage> findByGroupChatRoomGroupIdAndMessageTypeIdOrderBySendTimeAsc(Long groupId, int messageTypeId);
}
