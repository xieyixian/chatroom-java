package top.javahai.chatroom.service.impl;

import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import top.javahai.chatroom.entity.*;
import top.javahai.chatroom.service.MailSendLogService;
import top.javahai.chatroom.service.MessageService;
import java.util.Date;
import java.util.UUID;
import static top.javahai.chatroom.config.RabbitMQConfig.LOGGER;

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
        //添加消息记录
        LOGGER.info("send message of group chat");
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
