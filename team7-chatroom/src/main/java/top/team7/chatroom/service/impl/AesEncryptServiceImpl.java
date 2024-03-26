package top.team7.chatroom.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.team7.chatroom.repository.ConversationRepository;

@Service
public class AesEncryptServiceImpl {

    @Autowired
    ConversationRepository conversationRepository;

    public  String getKey(Long id){
       return conversationRepository.findKeyById(id);
    }


}
