package top.team7.chatroom.controller;

import top.team7.chatroom.entity.Admin;
import top.team7.chatroom.service.AdminService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("admin")
public class AdminController {

    @Resource
    private AdminService adminService;

    @GetMapping("selectOne")
    public Admin selectOne(Integer id) {
        return this.adminService.queryById(id);
    }

}