package top.team7.chatroom.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "group_chat_messages")
public class GroupChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id")
    private Long messageId;

    @ManyToOne
    @JoinColumn(name = "sender_user_name", referencedColumnName = "username")
    private User senderUser;

    @ManyToOne
    @JoinColumn(name = "group_id", referencedColumnName = "group_id")
    private GroupChatRoom groupChatRoom;

    @Column(name = "message_text")
    private String messageText;

    @Column(name = "send_time")
    private LocalDateTime sendTime;

    @Column(name = "read_time")
    private LocalDateTime readTime;

    @Column(name = "message_type_id")
    private int messageTypeId;

    // Getters and setters
    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public User getSenderUser() {
        return senderUser;
    }

    public void setSenderUser(User senderUser) {
        this.senderUser = senderUser;
    }

    public GroupChatRoom getGroupChatRoom() {
        return groupChatRoom;
    }

    public void setGroupChatRoom(GroupChatRoom groupChatRoom) {
        this.groupChatRoom = groupChatRoom;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public LocalDateTime getSendTime() {
        return sendTime;
    }

    public void setSendTime(LocalDateTime sendTime) {
        this.sendTime = sendTime;
    }

    public LocalDateTime getReadTime() {
        return readTime;
    }

    public void setReadTime(LocalDateTime readTime) {
        this.readTime = readTime;
    }

    public int getMessageTypeId() {
        return messageTypeId;
    }

    public void setMessageTypeId(int messageTypeId) {
        this.messageTypeId = messageTypeId;
    }
}
