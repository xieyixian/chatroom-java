package top.javahai.chatroom.receiver;


import com.alibaba.fastjson.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.impl.AMQImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

/**
 * 处理发送验证码的消息，保证消息不会重复消费
 *
 * @author Hai
 * @date 2020/10/2 - 23:25
 */
@Component
public class VerifyCodeReceiver {
    @Autowired
    JavaMailSender javaMailSender;
    @Autowired
    StringRedisTemplate redisTemplate;

    private static final Logger LOGGER = LoggerFactory.getLogger(VerifyCodeReceiver.class);

    //    @RabbitListener(queues = "${mail.queue.verifyCode:mail-queue-verifyCode}")
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "mail-queue-verifyCode"),
            exchange = @Exchange(name = "mail-exchange", type = ExchangeTypes.DIRECT),
            key = {"mailRouteVerifyCode"}
    ))
    public void getMessage(Message message, Channel channel) throws IOException {
        LOGGER.info("Verification code has been consumed");
        String info = message.getPayload().toString();
        MessageHeaders headers = message.getHeaders();
        Long tag = (Long) headers.get(AmqpHeaders.DELIVERY_TAG);
        String msgId = (String) headers.get("spring_returned_message_correlation");
        LOGGER.info("【" + msgId + "】-Message being processed");
        if (redisTemplate.opsForHash().entries("mail_log").containsKey(msgId)){
            channel.basicAck(tag,false);
            LOGGER.info("【"+msgId+"】Messages are consumed repeatedly");
            return;
        }


        info = info.replace("{", "").replace("}", "").replace("\"", "");
        String[] parts = info.split(":");
        String email = parts[0];
        String code = parts[1];

        System.out.println("Email: " + email);
        System.out.println("Code: " + code);


        try {

            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setSubject("Team7 Chat Room Management Terminal-Verification Code Verification");
            msg.setText("Verification code for this login：" + code);
            msg.setFrom("tow8se@163.com");
            msg.setSentDate(new Date());
            msg.setTo(email);
            javaMailSender.send(msg);
            redisTemplate.opsForHash().entries("mail_log").put(msgId,code);
            redisTemplate.opsForHash().put("mail_log",msgId,code);
            channel.basicAck(tag, false);
        } catch (Exception e) {
            channel.basicReject(tag, false);
            LOGGER.info("【" + msgId + "】Message was discarded");
            e.printStackTrace();
        }
    }
}
