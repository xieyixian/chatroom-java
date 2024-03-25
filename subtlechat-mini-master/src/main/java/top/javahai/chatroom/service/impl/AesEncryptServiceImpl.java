package top.javahai.chatroom.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.javahai.chatroom.repository.ConversationRepository;

@Service
public class AesEncryptServiceImpl {

    @Autowired
    ConversationRepository conversationRepository;

    public  String getKey(Long id){
       return conversationRepository.findKeyById(id);
    }


}
