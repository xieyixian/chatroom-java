package top.javahai.chatroom.controller;

import org.apache.ibatis.jdbc.Null;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;
import top.javahai.chatroom.config.VerificationCode;
import top.javahai.chatroom.entity.Admin;
import top.javahai.chatroom.entity.RespBean;
import top.javahai.chatroom.entity.User;
import top.javahai.chatroom.service.AdminService;
import top.javahai.chatroom.service.UserService;
import top.javahai.chatroom.service.impl.AdminServiceImpl;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Date;
import java.util.Enumeration;
import java.util.Random;

/**
 * @author Hai
 * @date 2020/6/16 - 17:33
 */
@RestController
public class LoginController {

  @Resource
  private AdminService adminService;
  @Resource
  private UserService userService;



  /**
   * 获取验证码图片写到响应的输出流中，保存验证码到session
   *
   * @param response
   * @param session
   * @throws IOException
   */
  @GetMapping("/verifyCode")
  public void getVerifyCode(HttpServletResponse response, HttpSession session) throws IOException {
    VerificationCode code = new VerificationCode();
    BufferedImage image = code.getImage();
    String text = code.getText();
    session.setAttribute("verify_code", text);
    VerificationCode.output(image, response.getOutputStream());
  }

  @Autowired
  JavaMailSender javaMailSender;


  public RespBean sendEmail(String param, String choice, String attribute_name, HttpSession session) {

    //生成验证码
    StringBuilder code = new StringBuilder();
    for (int i = 0; i < 4; i++) {
      code.append(new Random().nextInt(10));
    }
    System.out.println(code);
    // 邮件内容
    SimpleMailMessage msg = new SimpleMailMessage();
    msg.setSubject("微言聊天室管理端验证码验证");
    msg.setText("本次注册的验证码：" + code);
    msg.setFrom("tow8se@163.com");
    msg.setSentDate(new Date());
    System.out.println("==>send to email by providing "+choice);



    if (choice.equals("email")) {
      msg.setTo(param);
    } else if (choice.equals("username_admin")) {
      //获取前端用户名字符串
      param = (param).substring(13, param.length() - 2);
      System.out.println("Get Admin UserName:" + param);
      Admin adminSearch;
      adminSearch = this.adminService.queryByUserName(param);
      if (adminSearch == null){
        return RespBean.error("无法查询到管理员信息");
      }

      System.out.println("Get Admin email address:" + adminSearch.getEmail());
      msg.setTo(adminSearch.getEmail());
    } else if (choice.equals("username_user")) {

      param = (param).substring(13, param.length() - 2);
      System.out.println("Get User UserName:" + param);

      User userSearch;
      System.out.println("enter");
      userSearch = this.userService.queryByUserName(param);
      if (userSearch == null ){
        return RespBean.error("无法查询到用户信息");
      }else{
        String email = userSearch.getEmail();
        if (email == null){
          return RespBean.error("用户邮箱为空");
        }
        else{
        System.out.println("Get User email address:" + email);
        msg.setTo(userSearch.getEmail());
        }
      }

    } else {
      return RespBean.error("服务器出错啦！请稍后重试！");
    }

    // 保存验证码到本次会话
    session.setAttribute(attribute_name, code.toString());

    // 发送到邮箱
    try {
      javaMailSender.send(msg);
      System.out.println("ok\n");
      return RespBean.ok("验证码已发送到邮箱，请注意查看！");
    } catch (Exception e) {
      e.printStackTrace();
      return RespBean.error("服务器出错啦！请稍后重试！");
    }

  }


  /**
   * 获取邮箱验证码，并保存到本次会话
   *
   * @param session
   * @ param adminUsername
   */
  @PostMapping("/user/mailVerifyCode")
  @ResponseBody
  public RespBean getMailVerifyCode_register(@RequestBody String email, HttpSession session) {

    //获取前端email
    email = (email).substring(10, email.length() - 2);
    System.out.println("Get email address:" + email);
    return sendEmail(email, "email", "mail_verify_code_register", session);

  }

  @PostMapping("/user/loginMailVerifyCode")
  @ResponseBody
  public RespBean getMailVerifyCode_login(@RequestBody String username, HttpSession session) {

    return sendEmail(username, "username_user", "mail_verify_code_login", session);

  }



  /**
   * 获取邮箱验证码，并保存到本次会话
   *
   * @param session
   * @ param adminUsername
   */
  @PostMapping("/admin/mailVerifyCode")
  @ResponseBody
  public RespBean getMailVerifyCode(@RequestBody String adminUsername, HttpSession session) {
    return sendEmail(adminUsername, "username_admin", "mail_verify_code", session);
  }
}



