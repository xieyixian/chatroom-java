package top.javahai.chatroom.controller;

import org.yaml.snakeyaml.events.Event;
import top.javahai.chatroom.entity.RespBean;
import top.javahai.chatroom.entity.RespPageBean;
import top.javahai.chatroom.entity.User;
import top.javahai.chatroom.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;


@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;


    @PostMapping("/register")
    public RespBean addUser(@RequestBody User user, HttpServletRequest request){
        String verifyCode = ((String) request.getSession().getAttribute("mail_verify_code_register"));
        System.out.println("Verification code："+verifyCode);
        if(verifyCode != null  &&   verifyCode.equals(user.getMailCode())){
            if (userService.insert(user)==1){
                return RespBean.ok("registration success！");
            }else{
                return RespBean.error("registration failed！");
            }
        }else {
            return RespBean.error("Verification code error！");
        }

    }


    @GetMapping("/checkUsername")
    public Integer checkUsername(@RequestParam("username")String username){
        return userService.checkUsername(username);
    }


    @GetMapping("/checkNickname")
    public Integer checkNickname(@RequestParam("nickname") String nickname){
        return userService.checkNickname(nickname);
    }


    @GetMapping("selectOne")
    public User selectOne(Integer id) {
        return this.userService.queryById(id);
    }


    @GetMapping("/")
    public RespPageBean getAllUserByPage(@RequestParam(value = "page",defaultValue = "1") Integer page,
                                         @RequestParam(value = "size",defaultValue = "10") Integer size,
                                         String keyword,Integer isLocked){
        return userService.getAllUserByPage(page,size,keyword,isLocked);
    }


    @PutMapping("/")
    public RespBean changeLockedStatus(@RequestParam("id") Integer id,@RequestParam("isLocked") Boolean isLocked){
      if (userService.changeLockedStatus(id,isLocked)==1){
          return RespBean.ok("更新成功！");
      }else {
          return RespBean.error("更新失败！");
      }
    }

    /**
     * 删除单一用户
     * @author luo
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public RespBean deleteUser(@PathVariable Integer id){
        if (userService.deleteById(id)){
            return RespBean.ok("删除成功！");
        }
        else{
            return RespBean.error("删除失败！");
        }
    }

    /**
     * 批量删除用户
     * @author luo
     * @param ids
     * @return
     */
    @DeleteMapping("/")
    public RespBean deleteUserByIds(Integer[] ids){
        if (userService.deleteByIds(ids)==ids.length){
            return RespBean.ok("删除成功！");
        }else{
            return RespBean.error("删除失败！");
        }
    }
}
