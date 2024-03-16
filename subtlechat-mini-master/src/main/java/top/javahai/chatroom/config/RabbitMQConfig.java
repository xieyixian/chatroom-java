package top.javahai.chatroom.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import top.javahai.chatroom.entity.MailConstants;
import top.javahai.chatroom.service.MailSendLogService;


/**
 * 自定义消息发送RabbitTemplate
 * @author Hai
 * @date 2020/10/2 - 14:35
 */
@Configuration
public class RabbitMQConfig {

//    @Value("${mail.exchange:mail-exchange}")
//    private String mailExchange;
//
//    @Value("${mail.queue.verifyCode:mail-queue-verifyCode}")
//    private String mailQueueVerifyCode;
//
//    @Value("${mail.route.verifyCode:mail-route-verifyCode}")
//    private String mailRouteVerifyCode;
//
//    @Value("${mail.queue.feedback:mail-queue-feedback}")
//    private String mailQueueFeedback;
//
//    @Value("${mail.route.feedback:mail-route-feedback}")
//    private String mailRouteFeedback;

    @Autowired
    CachingConnectionFactory cachingConnectionFactory;

    @Autowired
    MailSendLogService mailSendLogService;

    public static final Logger LOGGER= LoggerFactory.getLogger(RabbitMQConfig.class);

//    @Bean
//    DirectExchange mailExchange(){
//        return new DirectExchange(mailExchange,true,false);
//    }
//
//    /**
//     * 验证码消息队列
//     * @return
//     */
//    @Bean
//    Queue mailQueueVerifyCode(){
//        return new Queue(mailQueueVerifyCode,true);
//    }
//
//    @Bean
//    Binding mailQueueVerifyCodeBinding(){
//        return BindingBuilder.bind(mailQueueVerifyCode()).to(mailExchange()).with(mailRouteVerifyCode);
//    }
//
//    /**
//     * 反馈消息队列
//     * @return
//     */
//    @Bean
//    Queue mailQueueFeedback(){
//        return new Queue(mailQueueFeedback,true);
//    }
//    @Bean
//    Binding mailQueueFeedbackBinding(){
//        return BindingBuilder.bind(mailQueueFeedback()).to(mailExchange()).with(mailRouteFeedback);
//    }

    @Bean
    public RabbitTemplate rabbitTemplate(){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(cachingConnectionFactory);
        //成功投递消息到Broker交换机站点的回调函数
        rabbitTemplate.setConfirmCallback((data,ack,cause)->{
            String msgId = data.getId();
            if(ack){
                LOGGER.info(msgId+"Message sent successfully");
                //修改数据库中的记录，消息发送成功，将status设为1
                mailSendLogService.updateMailSendLogStatus(msgId, MailConstants.SUCCESS);
            }else{
                LOGGER.error(msgId+"Message sending failure！");
            }
        });
        //消息投递到Queue队列失败的回调函数
        rabbitTemplate.setReturnCallback((msg,repCode,repText,exchange,routingKey)->{
            LOGGER.error(msg.getBody()+"---- Message failed to be delivered from switch to queue! Error cause:"+repText);
            LOGGER.error("Sending the wrong exchange："+exchange+",The route key is faulty. Procedure："+routingKey);
        });
        return rabbitTemplate;
    }
}
