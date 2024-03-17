package top.javahai.chatroom.controller;

import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import top.javahai.chatroom.entity.User;

import javax.annotation.Resource;

import java.util.concurrent.TimeUnit;

import static top.javahai.chatroom.constant.RedisKeyConstant.USER_CONTINUE_LIFE_KEY;

@Controller
public class HeartController {

    @Resource
    private RedissonClient redisson;

    @ResponseBody
    @RequestMapping("/continueLife")
    public String continueLife(@RequestBody User user){
        Integer uid = user.getId();
        if(uid == null){
            return "error";
        }
        RBucket<Object> bucket = redisson.getBucket(String.format(USER_CONTINUE_LIFE_KEY,uid));
        bucket.set("user_"+uid,8L, TimeUnit.SECONDS);
        return "OK";
    }

}
