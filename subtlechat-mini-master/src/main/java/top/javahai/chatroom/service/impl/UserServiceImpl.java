package top.javahai.chatroom.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import top.javahai.chatroom.dao.UserDao;
import top.javahai.chatroom.entity.RespPageBean;
import top.javahai.chatroom.entity.User;
import top.javahai.chatroom.repository.UserRepository;
import top.javahai.chatroom.service.UserService;
import org.springframework.stereotype.Service;
import top.javahai.chatroom.utils.UserUtil;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * (User)表服务实现类
 *
 * @author makejava
 * @since 2020-06-16 11:37:09
 */
@Service("userService")
public class UserServiceImpl implements UserService, UserDetailsService {
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Resource
    private UserDao userDao;

    /**
     * 根据用户名进行登录
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDao.loadUserByUsername(username);
        if (user==null){
            throw new UsernameNotFoundException("用户不存在");
        }
        return user;
    }

    /**
     * 获取除了当前用户的所有user表的数据
     * @return
     */
    @Override
    public List<User> getUsersWithoutCurrentUser() {
        return userDao.getUsersWithoutCurrentUser(UserUtil.getCurrentUser().getId());
    }
    /**
     * 设置用户当前状态为在线
     * @param id 用户id
     */
    @Override
    public void setUserStateToOn(Integer id) {
        userDao.setUserStateToOn(id);
    }
    /**
     * 设置用户当前状态为离线
     * @param id 用户id
     */
    @Override
    public void setUserStateToLeave(Integer id) {
        userDao.setUserStateToLeave(id);
    }

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public User queryById(Integer id) {
        return this.userDao.queryById(id);
    }

    @Override
    public User queryByUserName(String username) {
        return userDao.queryByUserName(username);
    }


    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    @Override
    public List<User> queryAllByLimit(int offset, int limit) {
        return this.userDao.queryAllByLimit(offset, limit);
    }

    /**
     * 新增数据
     *
     * @param user 实例对象
     * @return 实例对象
     */
    @Override
    public Integer insert(User user) {
        //对密码进行加密
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodePass = encoder.encode(user.getPassword());
        user.setPassword(encodePass);
        user.setUserStateId(2);
        user.setEnabled(true);
        user.setLocked(false);
        return  this.userDao.insert(user);
    }

    /**
     * 修改数据
     *
     * @param user 实例对象
     * @return 实例对象
     */
    @Override
    public Integer update(User user) {
        return this.userDao.update(user);
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
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
    public RespPageBean getAllUserByPage(Integer page, Integer size,String keyword,Integer isLocked) {
        if (page!=null&&size!=null){
            page=(page-1)*size;//起始下标
        }
        //获取用户数据
        List<User> userList=userDao.getAllUserByPage(page,size,keyword,isLocked);
        //获取用户数据的总数
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
        // 调用 repository 方法查找用户
        return userRepository.findByUsername(username);
    }

    public List<User> findByUsernames(List<String> usernames) {
        List<User> users = new ArrayList<>();
        for (String username : usernames) {
            Optional<User> userOptional = userRepository.findByUsername(username);
            userOptional.ifPresent(users::add); // 如果存在用户信息，则添加到列表中
        }
        return users;
    }

    @Override
    public User queryByUsername(String userName) {
        return userDao.queryByUsername(userName);
    }

}
