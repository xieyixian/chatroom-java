package top.team7.chatroom.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;
import top.team7.chatroom.config.VerificationCode;
import top.team7.chatroom.entity.Admin;
import top.team7.chatroom.entity.User;
import top.team7.chatroom.service.AdminService;
import top.team7.chatroom.service.UserService;
import top.team7.chatroom.service.VerifyCodeService;
import top.team7.chatroom.entity.RespBean;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

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


    String error_info;
    if (choice.equals("email")) {
      //msg.setTo(param);
      return param;
    } else if (choice.equals("username_admin")) {
      System.out.println(param);

      param = param.replace("{", "").replace("}", "").replace("\"", "");
      String[] parts = param.split(":");
      param = parts[1];

      //param = (param).substring(9);
      System.out.println("Get Admin UserName:" + param);
      Admin adminSearch;
      adminSearch = this.adminService.queryByUserName(param);
      if (adminSearch == null){
        error_info="Unable to find administrator information";
        return error_info;
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
        error_info =  "Unable to query user information";
        return error_info;
      }else{

        if (userSearch.getEmail() == null){
          error_info="User mailbox is empty";
          return error_info;
        }
        else{
          System.out.println("Get User email address:" + userSearch.getEmail());
          //msg.setTo(userSearch.getEmail());
          return userSearch.getEmail();
        }
      }

    } else {
      error_info="The server has an error! Please try again later!";
      return error_info;
    }
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
    //Generate verification code
    StringBuilder code = new StringBuilder();
    for (int i = 0; i < 4; i++) {
      code.append(new Random().nextInt(10));
    }
    System.out.println(code);
    //Get front-end email
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
  public RespBean getMailVerifyCode(@RequestBody String adminUsername, HttpSession session) {
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
