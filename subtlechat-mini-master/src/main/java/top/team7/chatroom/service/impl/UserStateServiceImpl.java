package top.team7.chatroom.service.impl;

import top.team7.chatroom.dao.UserStateDao;
import top.team7.chatroom.entity.UserState;
import top.team7.chatroom.service.UserStateService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("userStateService")
public class UserStateServiceImpl implements UserStateService {
    @Resource
    private UserStateDao userStateDao;

    @Override
    public UserState queryById(Integer id) {
        return this.userStateDao.queryById(id);
    }

    @Override
    public List<UserState> queryAllByLimit(int offset, int limit) {
        return this.userStateDao.queryAllByLimit(offset, limit);
    }


    @Override
    public UserState insert(UserState userState) {
        this.userStateDao.insert(userState);
        return userState;
    }


    @Override
    public UserState update(UserState userState) {
        this.userStateDao.update(userState);
        return this.queryById(userState.getId());
    }


    @Override
    public boolean deleteById(Integer id) {
        return this.userStateDao.deleteById(id) > 0;
    }
}