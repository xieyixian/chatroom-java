package top.team7.chatroom.config;

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
import top.team7.chatroom.entity.MailConstants;
import top.team7.chatroom.service.MailSendLogService;


@Configuration
public class RabbitMQConfig {

    @Value("${mail.exchange:mail-exchange}")
    private String mailExchange;

    @Value("${mail.queue.verifyCode:mail-queue-verifyCode}")
    private String mailQueueVerifyCode;

    @Value("${mail.route.verifyCode:mail-route-verifyCode}")
    private String mailRouteVerifyCode;

    @Value("${msg.route.verifyCode:msg-route-verifyCode}")
    private String msgRouteVerifyCode;


    @Value("${mail.queue.feedback:mail-queue-feedback}")
    private String mailQueueFeedback;

    @Value("${mail.route.feedback:mail-route-feedback}")
    private String mailRouteFeedback;

    @Value("${msg.exchange:msg-exchange}")
    private String msgExchange;

    @Value("${msg.queue:msg-queue}")
    private String msgQueue;





    @Autowired
    CachingConnectionFactory cachingConnectionFactory;

    @Autowired
    MailSendLogService mailSendLogService;

    public static final Logger LOGGER= LoggerFactory.getLogger(RabbitMQConfig.class);

    @Bean
    DirectExchange mailExchange(){
        return new DirectExchange(mailExchange,true,false);
    }

    @Bean
    DirectExchange msgExchange(){
        return new DirectExchange(msgExchange,true,false);
    }


    @Bean
    Queue mailQueueVerifyCode(){
        return new Queue(mailQueueVerifyCode,true);
    }

    @Bean
    Binding mailQueueVerifyCodeBinding(){
        return BindingBuilder.bind(mailQueueVerifyCode()).to(mailExchange()).with(mailRouteVerifyCode);
    }

    @Bean
    Queue mailQueueFeedback(){
        return new Queue(mailQueueFeedback,true);
    }

    @Bean
    Binding mailQueueFeedbackBinding(){
        return BindingBuilder.bind(mailQueueFeedback()).to(mailExchange()).with(mailRouteFeedback);
    }


    @Bean
    Queue msgQueue(){
        return new Queue(msgQueue,true);
    }

    @Bean
    Binding msgQueueBinding(){
        return BindingBuilder.bind(msgQueue()).to(msgExchange()).with(msgRouteVerifyCode);
    }



    @Bean
    public RabbitTemplate rabbitTemplate(){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(cachingConnectionFactory);
        rabbitTemplate.setConfirmCallback((data,ack,cause)->{
            String msgId = data.getId();
            if(ack){
                LOGGER.info(msgId+"Message sent successfully");
                mailSendLogService.updateMailSendLogStatus(msgId, MailConstants.SUCCESS);
            }else{
                LOGGER.error(msgId+"Message sending failure！");
            }
        });
        //Callback function for failure of message delivery to Queue queue
        rabbitTemplate.setReturnCallback((msg,repCode,repText,exchange,routingKey)->{
            LOGGER.error(msg.getBody()+"---- Message failed to be delivered from switch to queue! Error cause:"+repText);
            LOGGER.error("Sending the wrong exchange："+exchange+",The route key is faulty. Procedure："+routingKey);
        });
        return rabbitTemplate;
    }
}
