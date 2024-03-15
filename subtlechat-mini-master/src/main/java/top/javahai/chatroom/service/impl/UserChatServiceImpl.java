package top.javahai.chatroom.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import top.javahai.chatroom.entity.Conversation;
import top.javahai.chatroom.entity.User;
import top.javahai.chatroom.entity.UserConversation;
import top.javahai.chatroom.entity.UserMessage;
import top.javahai.chatroom.repository.ConversationRepository;
import top.javahai.chatroom.repository.UserConversationRepository;
import top.javahai.chatroom.repository.UserMessageRepository;
import top.javahai.chatroom.repository.UserRepository;
import top.javahai.chatroom.service.UserChatService;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserChatServiceImpl implements UserChatService {

    private final ConversationRepository conversationRepository;
    private final UserConversationRepository userConversationRepository;
    private final UserMessageRepository userMessageRepository;
    private final UserRepository userRepository;

    @Autowired
    public UserChatServiceImpl(ConversationRepository conversationRepository,
                               UserConversationRepository userConversationRepository,
                               UserMessageRepository userMessageRepository,
                               UserRepository userRepository) {
        this.conversationRepository = conversationRepository;
        this.userConversationRepository = userConversationRepository;
        this.userMessageRepository = userMessageRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Conversation createConversation(Conversation conversation) {
        return conversationRepository.save(conversation);
    }

    @Override
    public UserConversation joinConversation(String username, String otherUsername) {
        // 查找用户实体
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
        User otherUser = userRepository.findByUsername(otherUsername)
                .orElseThrow(() -> new RuntimeException("Other user not found with username: " + otherUsername));

        // 查找两个用户是否有共同的现有会话的ID
        List<Long> commonConversationIds = userConversationRepository.findCommonConversationsByUsername(username, otherUsername);

        // 定义会话变量
        Conversation conversation;

        // 如果没有找到共同的会话，则创建一个新会话
        if (commonConversationIds.isEmpty()) {
            conversation = new Conversation();
            conversation.setCreateTime(LocalDateTime.now());
            // 保存新的会话到数据库，JPA会填充ID
            conversation = conversationRepository.save(conversation);

        } else {
            // 如果找到了，获取现有的会话
            Long conversationId = commonConversationIds.get(0); // 获取共有会话的ID
            UserConversation UserConversation = userConversationRepository
                    .findByConversation_ConversationId(conversationId).get(0);
            return UserConversation;
        }

        // 检查当前用户是否已经在会话中
        UserConversation currentUserConversation = userConversationRepository
                .findByUserAndConversation(user, conversation)
                .orElse(null);

        // 如果当前用户还没有加入，则创建并保存UserConversation
        if (currentUserConversation == null) {
            currentUserConversation = new UserConversation();
            currentUserConversation.setUser(user);
            currentUserConversation.setConversation(conversation);
            currentUserConversation.setJoinTime(LocalDateTime.now());
            // 保存到数据库
            currentUserConversation = userConversationRepository.save(currentUserConversation);
        }

        // 对于其他用户重复相同的逻辑
        UserConversation otherUserConversation = userConversationRepository
                .findByUserAndConversation(otherUser, conversation)
                .orElse(null);

        if (otherUserConversation == null) {
            otherUserConversation = new UserConversation();
            otherUserConversation.setUser(otherUser);
            otherUserConversation.setConversation(conversation);
            otherUserConversation.setJoinTime(LocalDateTime.now());
            // 保存到数据库
            otherUserConversation = userConversationRepository.save(otherUserConversation);
        }

        // 返回其中一个UserConversation，通常是发起调用的用户的UserConversation
        return currentUserConversation;
    }


    @Override
    public List<UserMessage> getMessagesByConversation(Long conversationId) {
        return userMessageRepository.findByConversation_ConversationIdOrderBySendTimeDesc(conversationId);
    }

    @Override
    public Page<UserMessage> getMessagesByConversation(Long conversationId, Pageable pageable) {
        return userMessageRepository.findByConversation_ConversationIdOrderBySendTimeDesc(conversationId, pageable);
    }

    @Override
    public UserMessage sendMessage(String username, Long conversationId, String messageText , int mesageTypeId) {
        User senderUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new RuntimeException("Conversation not found with id: " + conversationId));

        UserMessage message = new UserMessage();
        message.setSenderUser(senderUser);
        message.setConversation(conversation);
        message.setMessageText(messageText);
        message.setSendTime(LocalDateTime.now());
        message.setMessageTypeId(mesageTypeId);
        return userMessageRepository.save(message);
    }
}
