package top.javahai.chatroom.aspect;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;
import top.javahai.chatroom.entity.GroupMsgContent;
import top.javahai.chatroom.entity.Message;
import top.javahai.chatroom.entity.User;
import top.javahai.chatroom.service.UserService;

import javax.annotation.Resource;

import java.util.concurrent.TimeUnit;

import static top.javahai.chatroom.constant.RedisKeyConstant.*;

/**
 * 限制聊天频率--2秒内禁止重复发送消息
 */
@Aspect
@Slf4j
@Component
public class LimiterAspect {

    @Resource
    private RedissonClient redisson;

    @Resource
    private UserService userService;


    @Pointcut("@annotation(org.springframework.messaging.handler.annotation.MessageMapping)")
    public void limiter(){}

    @Around("limiter()")
    public Object limitReq(ProceedingJoinPoint point){
        Object[] objects = point.getArgs();
        if(objects == null || objects.length != 2){
            try {
                return point.proceed(objects);
            } catch (Throwable throwable) {
                log.error("error:",throwable);
            }
            return "error";
        }
        Object obj = objects[1];
        if(obj instanceof Message){
            Message msg = (Message) obj;
            String from = msg.getFrom();
            String to = msg.getTo();
            User fromUser = userService.queryByUsername(from);
            if(fromUser == null){
                log.error("send message error: from:{}",from);
                return "error";
            }

            //判断是否是机器人聊天
            if(StringUtils.isBlank(to) || to.equals("机器人")){
                RBucket<Object> robotKey = redisson.getBucket(String.format(ROBOT_MSG_LIMIT_KEY,fromUser.getId()));
                if(robotKey.isExists()){
                    log.info("robot msg limit: from:{}",from);
                    return "limit";
                }
                //3秒内禁止连续发送消息
                robotKey.set(from,3, TimeUnit.SECONDS);
            }else{
                User toUser = userService.queryByUsername(to);
                if(toUser == null){
                    log.error("send message error: from:{},to:{}",from,to);
                    return "error";
                }
                RBucket<Object> privateKey = redisson.getBucket(String.format(PRIVATE_MSG_LIMIT_KEY,fromUser.getId(),
                        toUser.getId()));
                if(privateKey.isExists()){
                    log.info("private msg limit: from:{},to:{}",from,to);
                    return "limit";
                }
                //3秒内禁止连续发送消息
                privateKey.set(fromUser.getId()+"_"+toUser.getId(),3, TimeUnit.SECONDS);
            }
        }else if(obj instanceof GroupMsgContent){
            GroupMsgContent content = (GroupMsgContent) obj;
            Integer fromId = content.getFromId();
            if(fromId == null){
                return "error";
            }
            RBucket<Object> groupKey = redisson.getBucket(String.format(GROUP_MSG_LIMIT_KEY,fromId));
            if(groupKey.isExists()){
                log.info("group msg limit: fromId:{}",fromId);
                return "limit";
            }
            //3秒内禁止连续发送消息
            groupKey.set(fromId,3, TimeUnit.SECONDS);
        }
        try {
            return point.proceed(objects);
        } catch (Throwable throwable) {
            log.error("error:",throwable);
        }
        return "error";
    }

}
