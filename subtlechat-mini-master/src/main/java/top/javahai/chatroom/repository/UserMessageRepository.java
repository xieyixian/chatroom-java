package top.javahai.chatroom.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import top.javahai.chatroom.entity.UserMessage;

import java.util.List;

@Repository
public interface UserMessageRepository extends JpaRepository<UserMessage, Long> {
    // 根据会话ID获取所有消息，并按发送时间降序排序
    List<UserMessage> findByConversation_ConversationIdOrderBySendTimeDesc(Long conversationId);

    // 根据用户username和会话ID获取用户在特定会话中发送的所有消息，并按发送时间降序排序
    List<UserMessage> findBySenderUser_UsernameAndConversation_ConversationIdOrderBySendTimeDesc(String username, Long conversationId);

    // 使用分页查询根据会话ID获取消息，并按发送时间降序排序
    Page<UserMessage> findByConversation_ConversationIdOrderBySendTimeDesc(Long conversationId, Pageable pageable);

    // 使用分页查询根据用户username和会话ID获取用户在特定会话中发送的所有消息，并按发送时间降序排序
    Page<UserMessage> findBySenderUser_UsernameAndConversation_ConversationIdOrderBySendTimeDesc(String username, Long conversationId, Pageable pageable);
}
