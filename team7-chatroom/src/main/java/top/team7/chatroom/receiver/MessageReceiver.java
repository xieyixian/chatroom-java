package top.team7.chatroom.receiver;

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
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import top.team7.chatroom.entity.GroupMsgContent;


import java.io.IOException;
import java.util.Date;

@Component
public class MessageReceiver {
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageReceiver.class);



    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "msg-queue", durable = "true"),
            exchange = @Exchange(value = "msg-exchange", type = ExchangeTypes.DIRECT),
            key = "msgRouteVerifyCode"
    ))
    public void getMessage(Message message, Channel channel) throws IOException {

        LOGGER.info("Verification code has been consumed");

        String info = message.getPayload().toString();
        MessageHeaders headers = message.getHeaders();
        Long tag = (Long) headers.get(AmqpHeaders.DELIVERY_TAG);
        String msgId = (String) headers.get("spring_returned_message_correlation");
        LOGGER.info("【" + msgId + "】-Message being processed");

        LOGGER.info(info);
        try {

            String content = info.substring(info.indexOf("{") + 1, info.lastIndexOf("}"));


            String[] parts = content.split(", ");
            String[] values = new String[parts.length];
            for (int i = 0; i < parts.length; i++) {
                String[] keyValue = parts[i].split("=");
                values[i] = keyValue[1];
            }


            for (String value : values) {
                System.out.println("value：" + value);
            }

            LOGGER.info("Message processed successfully: " + msgId);
            GroupMsgContent groupMsgContent = new GroupMsgContent();
            int id = Integer.parseInt(values[0]);
            int formId = Integer.parseInt(values[1]);
            int msgTypeId =  Integer.parseInt(values[6]);
            int type =  Integer.parseInt(values[7]);
            groupMsgContent.setFromId(id);
            groupMsgContent.setFromId(formId);
            groupMsgContent.setFromName(values[2].substring(1, values[2].length() - 1));
            groupMsgContent.setFromProfile(values[3]);
            groupMsgContent.setCreateTime(new Date());
            groupMsgContent.setContent((values[5]).substring(1, values[5].length() - 1));
            groupMsgContent.setMessageTypeId(msgTypeId);
            groupMsgContent.setType(type);
            simpMessagingTemplate.convertAndSend("/topic/greetings",groupMsgContent);
            channel.basicAck(tag, false);
        } catch (Exception e) {
            // Reject and requeue message
            channel.basicAck(tag, false);
            LOGGER.error("Failed to process message: " + msgId, e);
        }
    }

}
