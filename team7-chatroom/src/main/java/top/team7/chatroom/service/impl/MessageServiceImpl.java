package top.team7.chatroom.service.impl;

import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import top.team7.chatroom.config.RabbitMQConfig;
import top.team7.chatroom.entity.GroupMsgContent;
import top.team7.chatroom.entity.MailConstants;
import top.team7.chatroom.entity.MailSendLog;
import top.team7.chatroom.service.MailSendLogService;
import top.team7.chatroom.service.MessageService;
import top.team7.chatroom.entity.*;

import java.util.Date;
import java.util.UUID;

@Service
public class MessageServiceImpl  implements MessageService {

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    MailSendLogService mailSendLogService;


    @Value("${msg.exchange:msg-exchange}")
    private String msgExchange;


    @Value("${msg.route.verifyCode:msg-route-verifyCode}")
    private String msgRouteVerifyCode;


    @Value("${userMsg.exchange:userMsg-exchange}")
    private String userMsgExchange;


    @Value("${msg.route.verifyCode:msg-route-verifyCode}")
    private String userMsgRouteVerifyCode;



    public void SendGroupMsg(GroupMsgContent groupMsgContent){


        String jsonMsgMap = groupMsgContent.toString();
        RabbitMQConfig.LOGGER.info("send message of group chat");
        String msgId = UUID.randomUUID().toString();
        MailSendLog mailSendLog = new MailSendLog();
        mailSendLog.setMsgId(msgId);
        mailSendLog.setContent(jsonMsgMap);
        mailSendLog.setContentType(MailConstants.VERIFY_Message_TYPE);
        mailSendLog.setCount(1);
        mailSendLog.setCreateTime(new Date());
        mailSendLog.setTryTime(new Date(System.currentTimeMillis()+1000*10*MailConstants.MEG_TIMEOUT));
        mailSendLog.setUpdateTime(new Date());
        mailSendLog.setExchange(msgExchange);
        mailSendLog.setRouteKey(msgRouteVerifyCode);
        mailSendLog.setStatus(MailConstants.DELIVERING);
        rabbitTemplate.convertAndSend(msgExchange,msgRouteVerifyCode,jsonMsgMap,new CorrelationData(msgId));

    }


}
