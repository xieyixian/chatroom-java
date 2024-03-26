package top.team7.chatroom.dao;

import org.apache.ibatis.annotations.Select;
import top.team7.chatroom.entity.User;
import org.apache.ibatis.annotations.Param;
import java.util.List;


public interface UserDao {

    User loadUserByUsername(String username);


    List<User> getUsersWithoutCurrentUser(Integer id);

    User queryById(Integer id);


    User queryByUserName(String username);

    List<User> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    List<User> queryAll(User user);

    int insert(User user);

    int update(User user);

    int deleteById(Integer id);

    void setUserStateToOn(Integer id);

    void setUserStateToLeave(Integer id);

    Integer checkUsername(String username);

    Integer checkNickname(String nickname);

    List<User> getAllUserByPage(@Param("page") Integer page, @Param("size") Integer size,String keyword,Integer isLocked);

    Long getTotal(@Param("keyword") String keyword,@Param("isLocked") Integer isLocked);

    Integer changeLockedStatus(@Param("id") Integer id, @Param("isLocked") Boolean isLocked);

  Integer deleteByIds(@Param("ids") Integer[] ids);

    @Select("select * from user where username = #{username}")
    User queryByUsername(@Param("username") String userName);
}
