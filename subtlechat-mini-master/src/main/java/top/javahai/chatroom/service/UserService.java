package top.javahai.chatroom.service;

import top.javahai.chatroom.entity.RespBean;
import top.javahai.chatroom.entity.RespPageBean;
import top.javahai.chatroom.entity.User;

import java.util.List;
import java.util.Optional;


public interface UserService {



    List<User> getUsersWithoutCurrentUser();


    public void setUserStateToOn(Integer id);

    public void setUserStateToLeave(Integer id);


    User queryById(Integer id);

    List<User> queryAllByLimit(int offset, int limit);

    Integer insert(User user);

    Integer update(User user);

    boolean deleteById(Integer id);

    Integer checkUsername(String username);

    Integer checkNickname(String nickname);

    RespPageBean getAllUserByPage(Integer page, Integer size,  String keyword,Integer isLocked);

    Integer changeLockedStatus(Integer id, Boolean isLocked);

    Integer deleteByIds(Integer[] ids);

    User queryByUsername(String userName);

    Optional<User> findByUsername(String currentUsername);

    List<User> findByUsernames(List<String> memberUsernames);

    User queryByUserName(String username);

}
