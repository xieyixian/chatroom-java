package top.javahai.chatroom.repository;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import top.javahai.chatroom.entity.Conversation;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Long> {
    // 自定义查询方法（如果需要）

    // 根据id查询key字段
    @Query("SELECT c.key FROM Conversation c WHERE c.conversationId = :id")
    String findKeyById(@Param("id") Long id);





}
