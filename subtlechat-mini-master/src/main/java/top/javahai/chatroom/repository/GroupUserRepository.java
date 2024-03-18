package top.javahai.chatroom.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import top.javahai.chatroom.entity.GroupUser;

import java.util.List;

@Repository
public interface GroupUserRepository extends JpaRepository<GroupUser, Long> {

    // 根据群组ID查询群组中的所有用户
    List<GroupUser> findByGroupChatRoomGroupId(Long groupId);

}
