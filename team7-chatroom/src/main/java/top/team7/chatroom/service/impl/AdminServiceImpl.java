package top.team7.chatroom.service.impl;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import top.team7.chatroom.dao.AdminDao;
import top.team7.chatroom.entity.Admin;
import top.team7.chatroom.service.AdminService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


@Service("adminService")
public class AdminServiceImpl implements AdminService, UserDetailsService {
    @Resource
    private AdminDao adminDao;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Admin admin=adminDao.loadUserByUsername(username);
        if (admin==null){
            throw new UsernameNotFoundException("The administrator cannot be found");
        }
        return admin;
    }

    @Override
    public Admin queryById(Integer id) {
        return this.adminDao.queryById(id);
    }



    @Override
    public Admin  queryByUserName(String username) {
        return this.adminDao.queryByUserName(username);
    }

    @Override
    public List<Admin> queryAllByLimit(int offset, int limit) {
        return this.adminDao.queryAllByLimit(offset, limit);
    }


    @Override
    public Admin insert(Admin admin) {
        this.adminDao.insert(admin);
        return admin;
    }

    @Override
    public Admin update(Admin admin) {
        this.adminDao.update(admin);
        return this.queryById(admin.getId());
    }


    @Override
    public boolean deleteById(Integer id) {
        return this.adminDao.deleteById(id) > 0;
    }

}
