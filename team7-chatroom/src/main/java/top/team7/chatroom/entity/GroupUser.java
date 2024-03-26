package top.team7.chatroom.entity;

import javax.persistence.*;

@Entity
@Table(name = "group_user")
public class GroupUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "relation_id")
    private Long relationId;

    @ManyToOne
    @JoinColumn(name = "username", referencedColumnName = "username")
    private User user;

    @ManyToOne
    @JoinColumn(name = "group_id", referencedColumnName = "group_id")
    private GroupChatRoom groupChatRoom;

    // Getters and setters
    public Long getRelationId() {
        return relationId;
    }

    public void setRelationId(Long relationId) {
        this.relationId = relationId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public GroupChatRoom getGroupChatRoom() {
        return groupChatRoom;
    }

    public void setGroupChatRoom(GroupChatRoom groupChatRoom) {
        this.groupChatRoom = groupChatRoom;
    }
}
