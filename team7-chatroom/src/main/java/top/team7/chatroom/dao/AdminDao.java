package top.team7.chatroom.dao;

import top.team7.chatroom.entity.Admin;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface AdminDao {


    Admin loadUserByUsername(String username);

    Admin queryById(Integer id);

    List<Admin> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    List<Admin> queryAll(Admin admin);

    int insert(Admin admin);

    int update(Admin admin);
    int deleteById(Integer id);

    Admin queryByUserName(String username);
}
