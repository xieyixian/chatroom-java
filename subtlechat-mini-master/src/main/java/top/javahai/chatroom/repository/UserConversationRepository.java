package top.javahai.chatroom.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import top.javahai.chatroom.entity.Conversation;
import top.javahai.chatroom.entity.User;
import top.javahai.chatroom.entity.UserConversation;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserConversationRepository extends JpaRepository<UserConversation, Long> {
    // 现有的根据用户ID找到用户参与的所有会话
    // List<UserConversation> findByUser_Id(Long userId);

    // 添加新方法：根据用户名找到用户参与的所有会话
    List<UserConversation> findByUser_Username(String username);

    // 修改现有方法：根据用户的username和会话的ID找到特定的参与关系
    UserConversation findByUser_UsernameAndConversation_ConversationId(String username, Long conversationId);

    // 根据用户的username和Conversation实体找到特定的参与关系
    Optional<UserConversation> findByUser_UsernameAndConversation(String username, Conversation conversation);

    @Query(value = "SELECT c.conversation_id FROM userconversations c INNER JOIN (SELECT conversation_id FROM userconversations WHERE username = :username1 OR username = :username2 GROUP BY conversation_id HAVING COUNT(DISTINCT username) = 2) as subquery ON c.conversation_id = subquery.conversation_id", nativeQuery = true)
    List<Long> findCommonConversationsByUsername(@Param("username1") String username1, @Param("username2") String username2);
    List<UserConversation> findByConversation_ConversationId(Long conversationId);

    Optional<UserConversation> findByUserAndConversation(User user, Conversation conversation);


}

