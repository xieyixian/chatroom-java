package top.javahai.chatroom.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import top.javahai.chatroom.config.VerificationCode;
import top.javahai.chatroom.entity.RespBean;
import top.javahai.chatroom.service.VerifyCodeService;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Date;
import java.util.Random;

/**
 * @author Hai
 * @date 2020/6/16 - 17:33
 */
@RestController
public class LoginController {

  @Autowired
  VerifyCodeService verifyCodeService;

  @GetMapping("/verifyCode")
  public void getVerifyCode(HttpServletResponse response, HttpSession session) throws IOException {
    VerificationCode code = new VerificationCode();
    BufferedImage image = code.getImage();
    String text = code.getText();
    session.setAttribute("verify_code",text);
    VerificationCode.output(image,response.getOutputStream());
  }

  @GetMapping("/admin/mailVerifyCode")
  public RespBean getMailVerifyCode(HttpSession session){
    String code = verifyCodeService.getVerifyCode();
    //保存验证码到本次会话
    session.setAttribute("mail_verify_code",code);
    verifyCodeService.sendVerifyCodeMail(code);
    return RespBean.ok("The verification code has been sent to the mailbox, please pay attention to check");
  }
}
