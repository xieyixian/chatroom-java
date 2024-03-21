package top.javahai.chatroom.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import top.javahai.chatroom.config.VerificationCode;
import top.javahai.chatroom.entity.Admin;
import top.javahai.chatroom.entity.RespBean;
import top.javahai.chatroom.entity.User;
import top.javahai.chatroom.service.AdminService;
import top.javahai.chatroom.service.UserService;
import top.javahai.chatroom.service.VerifyCodeService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @author Hai
 * @date 2020/6/16 - 17:33
 */
@RestController
public class LoginController {

  @Autowired
  VerifyCodeService verifyCodeService;

  @Resource
  private AdminService adminService;
  @Resource
  private UserService userService;

  @Autowired
  JavaMailSender javaMailSender;

  @GetMapping("/verifyCode")
  public void getVerifyCode(HttpServletResponse response, HttpSession session) throws IOException {
    VerificationCode code = new VerificationCode();
    BufferedImage image = code.getImage();
    String text = code.getText();
    session.setAttribute("verify_code",text);
    VerificationCode.output(image,response.getOutputStream());
  }

  public String getEmail(String param, String choice) {


    // 邮件内容
//    SimpleMailMessage msg = new SimpleMailMessage();
//    msg.setSubject("微言聊天室管理端验证码验证");
//    msg.setText("本次注册的验证码：" + code);
//    msg.setFrom("tow8se@163.com");
//    msg.setSentDate(new Date());
//    System.out.println("==>send to email by providing "+choice);

    String error_info;
    if (choice.equals("email")) {
      //msg.setTo(param);
      return param;
    } else if (choice.equals("username_admin")) {
      //获取前端用户名字符串
      System.out.println(param);

      param = param.replace("{", "").replace("}", "").replace("\"", "");
      String[] parts = param.split(":");
      param = parts[1];

      //param = (param).substring(9);
      System.out.println("Get Admin UserName:" + param);
      Admin adminSearch;
      adminSearch = this.adminService.queryByUserName(param);
      if (adminSearch == null){
        error_info="无法查询到管理员信息";
        return error_info;
        //return RespBean.error("无法查询到管理员信息");
      }

      System.out.println("Get Admin email address:" + adminSearch.getEmail());
      //msg.setTo(adminSearch.getEmail());
      return adminSearch.getEmail();
    } else if (choice.equals("username_user")) {

      param = (param).substring(13, param.length() - 2);
      System.out.println("Get User UserName:" + param);

      User userSearch;
      System.out.println("enter");
      userSearch = this.userService.queryByUserName(param);
      if (userSearch == null ){
        error_info =  "无法查询到用户信息";
        return error_info;
      }else{

        if (userSearch.getEmail() == null){
          error_info="用户邮箱为空";
          return error_info;
        }
        else{
          System.out.println("Get User email address:" + userSearch.getEmail());
          //msg.setTo(userSearch.getEmail());
          return userSearch.getEmail();
        }
      }

    } else {
      error_info="服务器出错啦！请稍后重试！";
      return error_info;
    }

    // 保存验证码到本次会话
//    session.setAttribute(attribute_name, code.toString());
//
//    // 发送到邮箱
//    try {
//      javaMailSender.send(msg);
//      System.out.println("ok\n");
//      return RespBean.ok("验证码已发送到邮箱，请注意查看！");
//    } catch (Exception e) {
//      e.printStackTrace();
//      return RespBean.error("服务器出错啦！请稍后重试！");
//    }

  }

  @PostMapping("/user/loginMailVerifyCode")
  @ResponseBody
  public RespBean getMailVerifyCode_login(@RequestBody String username, HttpSession session) {



    StringBuilder code = new StringBuilder();
    for (int i = 0; i < 4; i++) {
      code.append(new Random().nextInt(10));
    }
    System.out.println(code);

    Map<String, String> map = new HashMap<>();
    map.put(getEmail(username, "username_user"), String.valueOf(code));

    session.setAttribute("mail_verify_code_login",String.valueOf(code));

    verifyCodeService.sendVerifyCodeMail(map);
    return RespBean.ok("The verification code has been sent to the mailbox, please pay attention to check");



  }


  @PostMapping("/user/mailVerifyCode")
  @ResponseBody
  public RespBean getMailVerifyCode_register(@RequestBody String email, HttpSession session) {
    //生成验证码
    StringBuilder code = new StringBuilder();
    for (int i = 0; i < 4; i++) {
      code.append(new Random().nextInt(10));
    }
    System.out.println(code);
    //获取前端email
    email = (email).substring(10, email.length() - 2);
    System.out.println("Get email address:" + email);


    Map<String, String> map = new HashMap<>();
    map.put(email, String.valueOf(code));

    session.setAttribute("mail_verify_code_register",String.valueOf(code));

    verifyCodeService.sendVerifyCodeMail(map);

    return RespBean.ok("The verification code has been sent to the mailbox, please pay attention to check");


    //return sendEmail(email, "email", "mail_verify_code_register", session);
  }



  @PostMapping("/admin/mailVerifyCode")
  @ResponseBody
  public RespBean getMailVerifyCode(@RequestBody String adminUsername, HttpSession session) {    //生成验证码
    StringBuilder code = new StringBuilder();
    for (int i = 0; i < 4; i++) {
      code.append(new Random().nextInt(10));
    }
    System.out.println(code);

    Map<String, String> map = new HashMap<>();
    map.put(getEmail(adminUsername, "username_admin"), String.valueOf(code));

    session.setAttribute("mail_verify_code",String.valueOf(code));

    verifyCodeService.sendVerifyCodeMail(map);
    return RespBean.ok("The verification code has been sent to the mailbox, please pay attention to check");

    //return sendEmail(adminUsername, "username_admin", "mail_verify_code", session);
  }


}
