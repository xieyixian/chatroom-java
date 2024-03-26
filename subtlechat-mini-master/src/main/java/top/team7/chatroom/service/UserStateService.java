package top.team7.chatroom.service;

import top.team7.chatroom.entity.UserState;
import java.util.List;


public interface UserStateService {


    UserState queryById(Integer id);

    List<UserState> queryAllByLimit(int offset, int limit);


    UserState insert(UserState userState);


    UserState update(UserState userState);


    boolean deleteById(Integer id);

}