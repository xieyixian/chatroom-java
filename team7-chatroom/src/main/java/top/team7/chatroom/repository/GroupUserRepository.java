package top.team7.chatroom.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import top.team7.chatroom.entity.GroupUser;

import java.util.List;

@Repository
public interface GroupUserRepository extends JpaRepository<GroupUser, Long> {


    List<GroupUser> findByGroupChatRoomGroupId(Long groupId);

}
