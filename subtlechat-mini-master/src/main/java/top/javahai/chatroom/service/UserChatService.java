package top.javahai.chatroom.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import top.javahai.chatroom.entity.Conversation;
import top.javahai.chatroom.entity.UserConversation;
import top.javahai.chatroom.entity.UserMessage;

import java.security.NoSuchAlgorithmException;
import java.util.List;

public interface UserChatService {
    Conversation createConversation(Conversation conversation);

    // 修改接口方法，使用username
    UserConversation joinConversation(String username, String otherUsername) throws NoSuchAlgorithmException;

    List<UserMessage> getMessagesByConversation(Long conversationId);

    Page<UserMessage> getMessagesByConversation(Long conversationId, Pageable pageable);

    // 修改接口方法，使用username
    UserMessage sendMessage(String username, Long conversationId, String messageText, int mesageTypeId);
}
