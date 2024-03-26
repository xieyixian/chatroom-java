package top.team7.chatroom.controller;

import top.team7.chatroom.entity.UserState;
import top.team7.chatroom.service.UserStateService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


@RestController
@RequestMapping("userState")
public class UserStateController {

    @Resource
    private UserStateService userStateService;


    @GetMapping("selectOne")
    public UserState selectOne(Integer id) {
        return this.userStateService.queryById(id);
    }

}