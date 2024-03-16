package top.javahai.chatroom.controller;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;
import top.javahai.chatroom.entity.Feedback;
import top.javahai.chatroom.entity.RespBean;
import top.javahai.chatroom.service.FeedbackService;

import java.util.Date;

@RestController
@RequestMapping("/mail")
public class MailController {

  @Autowired
  FeedbackService feedbackService;

  @PostMapping("/feedback")
  public RespBean sendFeedbackToMail(@RequestBody Feedback feedback){
    try{
      feedbackService.sendMessage(feedback);
    }catch (Exception e){
      e.printStackTrace();
    }finally {
      return RespBean.ok("Email sent successfully! Thank you for your feedback");
    }
  }
}
