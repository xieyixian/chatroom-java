package top.team7.chatroom.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import top.team7.chatroom.entity.UserMessage;

import java.util.List;

@Repository
public interface UserMessageRepository extends JpaRepository<UserMessage, Long> {

    List<UserMessage> findByConversation_ConversationIdOrderBySendTimeDesc(Long conversationId);


    List<UserMessage> findBySenderUser_UsernameAndConversation_ConversationIdOrderBySendTimeDesc(String username, Long conversationId);


    Page<UserMessage> findByConversation_ConversationIdOrderBySendTimeDesc(Long conversationId, Pageable pageable);

    Page<UserMessage> findBySenderUser_UsernameAndConversation_ConversationIdOrderBySendTimeDesc(String username, Long conversationId, Pageable pageable);
}
