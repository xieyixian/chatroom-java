package top.team7.chatroom.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import top.team7.chatroom.entity.GroupChatRoom;
import top.team7.chatroom.entity.User;

import java.util.List;

@Repository
public interface GroupChatRoomRepository extends JpaRepository<GroupChatRoom, Long> {


    List<GroupChatRoom> findByCreatorUsername(String creatorUsername);


    List<GroupChatRoom> findByGroupNameContaining(String groupName);

    List<GroupChatRoom> findByGroupUsers_User(User user);
}
