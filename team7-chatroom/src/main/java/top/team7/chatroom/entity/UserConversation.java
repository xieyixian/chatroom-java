package top.team7.chatroom.entity;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "userconversations")
public class UserConversation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_conversationid")
    private Long userConversationId;

    @ManyToOne
    @JoinColumn(name = "username", referencedColumnName = "username")
    private User user;

    @ManyToOne
    @JoinColumn(name = "conversation_id", referencedColumnName = "conversation_id")
    private Conversation conversation;
    @Column(name = "join_time")
    private LocalDateTime joinTime;

    // Getters and setters
    public Long getUserConversationId() {
        return userConversationId;
    }

    public void setUserConversationId(Long userConversationId) {
        this.userConversationId = userConversationId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Conversation getConversation() {
        return conversation;
    }

    public void setConversation(Conversation conversation) {
        this.conversation = conversation;
    }

    public LocalDateTime getJoinTime() {
        return joinTime;
    }

    public void setJoinTime(LocalDateTime joinTime) {
        this.joinTime = joinTime;
    }

}
