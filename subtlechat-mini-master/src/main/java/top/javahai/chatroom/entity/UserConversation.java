package top.javahai.chatroom.entity;
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
//    @Column(name = "username")
//    private String username; // 确保这里是一个字符串字段
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
//    public void setUserName(String username) {
//        this.username = username;
//    }
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
//    public void setUsername(String username) {
//        if (this.user == null) {
//            this.user = new User();
//        }
//        this.user.setUsername(username);
//    }
//    public void setConversationId(Long conversationId) {
//        if (this.conversation == null) {
//            this.conversation = new Conversation();
//        }
//        this.conversation.setConversationId(conversationId);
//    }

}
