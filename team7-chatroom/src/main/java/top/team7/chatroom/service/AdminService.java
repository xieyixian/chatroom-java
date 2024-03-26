package top.team7.chatroom.service;

import top.team7.chatroom.entity.Admin;
import java.util.List;


public interface AdminService {


    Admin queryById(Integer id);



    Admin queryByUserName(String username);

    List<Admin> queryAllByLimit(int offset, int limit);

    Admin insert(Admin admin);

    Admin update(Admin admin);

    boolean deleteById(Integer id);

}