package top.team7.chatroom.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import top.team7.chatroom.entity.Conversation;
import top.team7.chatroom.entity.User;
import top.team7.chatroom.entity.UserConversation;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserConversationRepository extends JpaRepository<UserConversation, Long> {

    List<UserConversation> findByUser_Username(String username);


    UserConversation findByUser_UsernameAndConversation_ConversationId(String username, Long conversationId);


    Optional<UserConversation> findByUser_UsernameAndConversation(String username, Conversation conversation);

    @Query(value = "SELECT c.conversation_id FROM userconversations c INNER JOIN (SELECT conversation_id FROM userconversations WHERE username = :username1 OR username = :username2 GROUP BY conversation_id HAVING COUNT(DISTINCT username) = 2) as subquery ON c.conversation_id = subquery.conversation_id", nativeQuery = true)
    List<Long> findCommonConversationsByUsername(@Param("username1") String username1, @Param("username2") String username2);
    List<UserConversation> findByConversation_ConversationId(Long conversationId);

    Optional<UserConversation> findByUserAndConversation(User user, Conversation conversation);


}

