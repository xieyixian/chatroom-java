package top.team7.chatroom.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import top.team7.chatroom.dao.UserDao;
import top.team7.chatroom.entity.RespPageBean;
import top.team7.chatroom.entity.User;
import top.team7.chatroom.utils.UserUtil;
import top.team7.chatroom.repository.UserRepository;
import top.team7.chatroom.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service("userService")
public class UserServiceImpl implements UserService, UserDetailsService {
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Resource
    private UserDao userDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDao.loadUserByUsername(username);
        if (user==null){
            throw new UsernameNotFoundException("User does not exist");
        }
        return user;
    }

    @Override
    public List<User> getUsersWithoutCurrentUser() {
        return userDao.getUsersWithoutCurrentUser(UserUtil.getCurrentUser().getId());
    }

    @Override
    public void setUserStateToOn(Integer id) {
        userDao.setUserStateToOn(id);
    }

    @Override
    public void setUserStateToLeave(Integer id) {
        userDao.setUserStateToLeave(id);
    }

    @Override
    public User queryById(Integer id) {
        return this.userDao.queryById(id);
    }

    @Override
    public User queryByUserName(String username) {
        return userDao.queryByUserName(username);
    }

    @Override
    public List<User> queryAllByLimit(int offset, int limit) {
        return this.userDao.queryAllByLimit(offset, limit);
    }

    @Override
    public Integer insert(User user) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodePass = encoder.encode(user.getPassword());
        user.setPassword(encodePass);
        user.setUserStateId(2);
        user.setEnabled(true);
        user.setLocked(false);
        return  this.userDao.insert(user);
    }

    @Override
    public Integer update(User user) {
        return this.userDao.update(user);
    }

    @Override
    public boolean deleteById(Integer id) {
        return this.userDao.deleteById(id) > 0;
    }

    @Override
    public Integer checkUsername(String username) {
        return userDao.checkUsername(username);
    }

    @Override
    public Integer checkNickname(String nickname) {
        return userDao.checkNickname(nickname);
    }

    @Override
    public RespPageBean getAllUserByPage(Integer page, Integer size, String keyword, Integer isLocked) {
        if (page!=null&&size!=null){
            page=(page-1)*size;
        }
        List<User> userList=userDao.getAllUserByPage(page,size,keyword,isLocked);
        Long total=userDao.getTotal(keyword,isLocked);
        RespPageBean respPageBean = new RespPageBean();
        respPageBean.setData(userList);
        respPageBean.setTotal(total);
        return respPageBean;
    }

    @Override
    public Integer changeLockedStatus(Integer id, Boolean isLocked) {
        return userDao.changeLockedStatus(id,isLocked);
    }

  @Override
  public Integer deleteByIds(Integer[] ids) {
    return userDao.deleteByIds(ids);
  }
    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public List<User> findByUsernames(List<String> usernames) {
        List<User> users = new ArrayList<>();
        for (String username : usernames) {
            Optional<User> userOptional = userRepository.findByUsername(username);
            userOptional.ifPresent(users::add);
        }
        return users;
    }

    @Override
    public User queryByUsername(String userName) {
        return userDao.queryByUsername(userName);
    }

}
