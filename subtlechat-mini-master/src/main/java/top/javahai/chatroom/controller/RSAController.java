package top.javahai.chatroom.controller;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.javahai.chatroom.utils.RSAUtil;

@RestController
public class RSAController {

    @RequestMapping("/getPublicKey")
    public String getPublicKey(){
        System.out.println("Generate public key:");
        System.out.println(RSAUtil.getPublicKey());
        return RSAUtil.getPublicKey();
    }
}
