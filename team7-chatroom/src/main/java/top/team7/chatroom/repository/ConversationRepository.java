package top.team7.chatroom.repository;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import top.team7.chatroom.entity.Conversation;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Long> {

    @Query("SELECT c.key FROM Conversation c WHERE c.conversationId = :id")
    String findKeyById(@Param("id") Long id);





}
