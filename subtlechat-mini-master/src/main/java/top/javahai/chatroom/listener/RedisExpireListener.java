package top.javahai.chatroom.listener;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import top.javahai.chatroom.entity.User;
import top.javahai.chatroom.service.impl.UserServiceImpl;

import javax.annotation.Resource;

/**
 * pub/sub获取redis过期key
 */
@Slf4j
@Component
public class RedisExpireListener extends KeyExpirationEventMessageListener {

    @Resource
    private SimpMessagingTemplate simpMessagingTemplate;
    @Resource
    private UserServiceImpl userService;

    public RedisExpireListener(RedisMessageListenerContainer listenerContainer) {
        super(listenerContainer);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String msg = message.toString();
        if(StringUtils.isNotBlank(msg) && msg.startsWith("userContinueLife_")){
            String uidStr = msg.split("_")[1];
            if(StringUtils.isBlank(uidStr)){
                log.info("uid:{} is blank:",uidStr);
                return;
            }

            User user = userService.queryById(Integer.parseInt(uidStr));
            if(user == null){
                log.info("uid:{} is blank:",uidStr);
                return;
            }

            //更新用户状态为离线
            userService.setUserStateToLeave(user.getId());

            //广播系统消息
            simpMessagingTemplate.convertAndSend("/topic/notification","系统消息：用户【"+user.getNickname()+"】离线");
        }
    }
}
