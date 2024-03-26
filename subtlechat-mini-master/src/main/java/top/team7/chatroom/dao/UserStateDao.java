package top.team7.chatroom.dao;

import top.team7.chatroom.entity.UserState;
import org.apache.ibatis.annotations.Param;
import java.util.List;


public interface UserStateDao {

    UserState queryById(Integer id);

    List<UserState> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    List<UserState> queryAll(UserState userState);

    int insert(UserState userState);

    int update(UserState userState);

    int deleteById(Integer id);

}