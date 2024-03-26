package top.javahai.chatroom.receiver;

import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;
import top.javahai.chatroom.entity.Feedback;
import top.javahai.chatroom.utils.JsonUtil;


import java.io.IOException;
import java.util.Date;

import static top.javahai.chatroom.config.RabbitMQConfig.LOGGER;


@Component
public class FeedbackReceiver {

    private static final Logger LOGGER = LoggerFactory.getLogger(FeedbackReceiver.class);

    @Autowired
    JavaMailSender javaMailSender;
//    @Autowired
//    StringRedisTemplate redisTemplate;

//    @RabbitListener(queues ="${mail.queue.feedback:mail-queue-feedback}")
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "mail-queue-feedback"),
            exchange = @Exchange(name = "mail-exchange", type = ExchangeTypes.DIRECT),
            key = {"mailRouteFeedback"}
    ))
    public void getFeedbackMessage(Message message, Channel channel) throws IOException {
        //获取消息内容
        LOGGER.info("feedback to consume");
        String s = message.getPayload().toString();
        //获取消息的唯一标志
        MessageHeaders headers = message.getHeaders();
        Long tag = ((Long) headers.get(AmqpHeaders.DELIVERY_TAG));
        String msgId = headers.get("spring_returned_message_correlation").toString();
        LOGGER.info("【" + msgId + "】-Message being processed");
        //是否已消费

        try {
            Feedback feedback = JsonUtil.parseToObject(s, Feedback.class);
            System.out.println(feedback.getContent());
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setSubject("Feedback from users");
            //读取信息
            StringBuilder formatMessage = new StringBuilder();
            formatMessage.append("user ID：" + feedback.getUserId() + "\n");
            formatMessage.append("username：" + feedback.getUsername() + "\n");
            formatMessage.append("User's Nickname：" + feedback.getNickname() + "\n");
            formatMessage.append("Feedback content：" + feedback.getContent());
            System.out.println(">>>>>>>>>>>>>>" + formatMessage + "<<<<<<<<<<<<<<<<<<");
            //设置邮件消息
            mailMessage.setText(formatMessage.toString());
            mailMessage.setFrom("Email address to send");
            mailMessage.setTo("Received email address");
            mailMessage.setSentDate(new Date());
            javaMailSender.send(mailMessage);
            //消息处理完成
            // redisTemplate.opsForHash().entries("mail_log").put(msgId,msgId+1);
//            redisTemplate.opsForHash().put("mail_log",msgId,feedback.getContent());
            //手动确定消息处理完成
            channel.basicAck(tag, true);
        } catch (Exception e) {
            //出现异常就重新放回到队列中
            channel.basicNack(tag, false, true);
            LOGGER.info("【" + msgId + "】The message is put back into the queue");
            e.printStackTrace();
        }

    }
}
