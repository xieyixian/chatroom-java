package top.javahai.chatroom.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import top.javahai.chatroom.entity.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // 根据用户名查找用户
    Optional<User> findByUsername(String username);

    Optional<User> findById(int id);
    // 其他自定义查询方法
    // ...
}

