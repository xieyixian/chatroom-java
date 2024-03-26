package top.team7.chatroom.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import top.team7.chatroom.entity.Conversation;
import top.team7.chatroom.entity.UserConversation;
import top.team7.chatroom.entity.UserMessage;

import java.security.NoSuchAlgorithmException;
import java.util.List;

public interface UserChatService {
    Conversation createConversation(Conversation conversation);


    UserConversation joinConversation(String username, String otherUsername) throws NoSuchAlgorithmException;

    List<UserMessage> getMessagesByConversation(Long conversationId);

    Page<UserMessage> getMessagesByConversation(Long conversationId, Pageable pageable);

    UserMessage sendMessage(String username, Long conversationId, String messageText, int mesageTypeId);
}
