package top.javahai.chatroom.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import top.javahai.chatroom.entity.GroupChatRoom;
import top.javahai.chatroom.entity.User;

import java.util.List;

@Repository
public interface GroupChatRoomRepository extends JpaRepository<GroupChatRoom, Long> {

    // 根据创建者用户名查询创建的所有群聊
    List<GroupChatRoom> findByCreatorUsername(String creatorUsername);

    // 根据群聊名称模糊查询群聊
    List<GroupChatRoom> findByGroupNameContaining(String groupName);

    List<GroupChatRoom> findByGroupUsers_User(User user);
}
