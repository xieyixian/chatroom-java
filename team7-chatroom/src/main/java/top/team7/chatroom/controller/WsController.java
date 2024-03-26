package top.team7.chatroom.controller;

import com.github.binarywang.java.emoji.EmojiConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import top.team7.chatroom.service.GroupMsgContentService;
import top.team7.chatroom.service.MessageService;
import top.team7.chatroom.entity.GroupMsgContent;
import top.team7.chatroom.entity.Message;
import top.team7.chatroom.entity.User;
import top.team7.chatroom.service.UserChatService;
import top.team7.chatroom.service.impl.AesEncryptServiceImpl;
import top.team7.chatroom.utils.AesEncryptUtil;

import java.text.SimpleDateFormat;
import java.util.Date;


@Controller
public class WsController {
  @Autowired
  SimpMessagingTemplate simpMessagingTemplate;

  @Autowired
  private UserChatService userChatService;

  @Autowired
  MessageService messageService;
  @Autowired
  AesEncryptServiceImpl aesEncryptService;

  @MessageMapping("/ws/chat")
  public void handlePrivateMessage(Authentication authentication, Message message) throws Exception {


    String key = aesEncryptService.getKey(message.getConversationId());
    AesEncryptUtil aesEncryptUtil = new AesEncryptUtil(key);


    User user = (User) authentication.getPrincipal();
    message.setFromNickname(user.getNickname());
    message.setFrom(user.getUsername());
    message.setCreateTime(new Date());

    String desContent;
    if(message.getMessageTypeId()==1) {
      desContent = aesEncryptUtil.desEncrypt(message.getContent(), aesEncryptUtil.getKEY(), aesEncryptUtil.getIV());
    }
    else{
      desContent=message.getContent();
    }
    message.setContent(desContent);
    System.out.println("message been des "+message.getContent());

    System.out.println(message.getContent());
    userChatService.sendMessage(
            user.getUsername(),
            message.getConversationId(),
            message.getContent(),
            message.getMessageTypeId()
    );

    if(message.getMessageTypeId()==1) {
      message.setContent(aesEncryptUtil.encrypt(message.getContent(),aesEncryptUtil.getKEY(),aesEncryptUtil.getIV()));
    }
    else{

    }

    System.out.println("message need to be sent "+message.getContent());


    simpMessagingTemplate.convertAndSendToUser(message.getTo(),"/queue/chat",message);

  }


  @Autowired
  GroupMsgContentService groupMsgContentService;
  EmojiConverter emojiConverter = EmojiConverter.getInstance();

  SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

  @MessageMapping("/ws/groupChat")
  public void handleGroupMessage(Authentication authentication, GroupMsgContent groupMsgContent) throws Exception {
    User currentUser= (User) authentication.getPrincipal();

    groupMsgContent.setFromId(currentUser.getId());
    groupMsgContent.setFromName(currentUser.getNickname());
    groupMsgContent.setFromProfile(currentUser.getUserProfile());
    groupMsgContent.setCreateTime(new Date());
    groupMsgContent.setContent(groupMsgContent.getContent());
    groupMsgContent.setType(groupMsgContent.getType());

    groupMsgContentService.insert(groupMsgContent);
    messageService.SendGroupMsg(groupMsgContent);
    groupMsgContentService.deleteGroupMsgById(groupMsgContent);
  }

}
