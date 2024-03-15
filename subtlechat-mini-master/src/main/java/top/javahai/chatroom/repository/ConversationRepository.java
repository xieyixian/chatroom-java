package top.javahai.chatroom.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import top.javahai.chatroom.entity.Conversation;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Long> {
    // 自定义查询方法（如果需要）
}
