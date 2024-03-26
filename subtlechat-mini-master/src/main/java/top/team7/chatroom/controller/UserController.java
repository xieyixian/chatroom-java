package top.team7.chatroom.controller;

import top.team7.chatroom.entity.RespPageBean;
import top.team7.chatroom.entity.User;
import top.team7.chatroom.entity.RespBean;
import top.team7.chatroom.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;


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
                                         String keyword, Integer isLocked){
        return userService.getAllUserByPage(page,size,keyword,isLocked);
    }


    @PutMapping("/")
    public RespBean changeLockedStatus(@RequestParam("id") Integer id,@RequestParam("isLocked") Boolean isLocked){
      if (userService.changeLockedStatus(id,isLocked)==1){
          return RespBean.ok("update completed！");
      }else {
          return RespBean.error("Update failed！");
      }
    }

    @DeleteMapping("/{id}")
    public RespBean deleteUser(@PathVariable Integer id){
        if (userService.deleteById(id)){
            return RespBean.ok("successfully deleted！");
        }
        else{
            return RespBean.error("failed to delete！");
        }
    }


    @DeleteMapping("/")
    public RespBean deleteUserByIds(Integer[] ids){
        if (userService.deleteByIds(ids)==ids.length){
            return RespBean.ok("successfully deleted！");
        }else{
            return RespBean.error("failed to delete！");
        }
    }
}
