package top.javahai.chatroom.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.javahai.chatroom.repository.ConversationRepository;
import top.javahai.chatroom.utils.RSAUtil;

@RestController
public class KeyController {
    @Autowired
    ConversationRepository conversationRepository;

    @RequestMapping("/getPublicKey")
    public String getPublicKey(){
        System.out.println("Generate public key:");
        System.out.println(RSAUtil.getPublicKey());
        return RSAUtil.getPublicKey();
    }

//    @PostMapping("/getAESKey")
//    public String getKeyAndIv(@RequestBody Long userConversationId){
//        System.out.println("Get key and iv:");
//        System.out.println( conversationRepository.findKeyById(userConversationId));
//
//        return  conversationRepository.findKeyById(userConversationId);
//    }
    @PostMapping("/getAESKey")
    public String getKeyAndIv(@RequestParam Long userConversationId) {
        System.out.println("Get key and iv:");
        System.out.println(conversationRepository.findKeyById(userConversationId));
        return conversationRepository.findKeyById(userConversationId);
    }
}
