package top.team7.chatroom.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import top.team7.chatroom.entity.Conversation;
import top.team7.chatroom.entity.User;
import top.team7.chatroom.entity.UserConversation;
import top.team7.chatroom.entity.UserMessage;
import top.team7.chatroom.repository.ConversationRepository;
import top.team7.chatroom.repository.UserConversationRepository;
import top.team7.chatroom.repository.UserMessageRepository;
import top.team7.chatroom.repository.UserRepository;
import top.team7.chatroom.service.UserChatService;

import java.security.NoSuchAlgorithmException;
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
    public UserConversation joinConversation(String username, String otherUsername) throws NoSuchAlgorithmException {
        // Find user entity
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
        User otherUser = userRepository.findByUsername(otherUsername)
                .orElseThrow(() -> new RuntimeException("Other user not found with username: " + otherUsername));

        // Find if two users have a common ID of an existing session
        List<Long> commonConversationIds = userConversationRepository.findCommonConversationsByUsername(username, otherUsername);

            // Define session variables
        Conversation conversation;

        // If no common session is found, create a new session
        if (commonConversationIds.isEmpty()) {
            conversation = new Conversation();
            conversation.setKey(conversation.generateKey());
            conversation.setCreateTime(LocalDateTime.now());
            System.out.println(conversation.getKey());
            // Save the new session to the database and JPA will populate the ID
            conversation = conversationRepository.save(conversation);

        } else {
            // If found, get the existing session
            Long conversationId = commonConversationIds.get(0); // Get the ID of a shared session
            UserConversation UserConversation = userConversationRepository
                    .findByConversation_ConversationId(conversationId).get(0);
            return UserConversation;
        }

        // Check if the current user is already in the session
        UserConversation currentUserConversation = userConversationRepository
                .findByUserAndConversation(user, conversation)
                .orElse(null);

        //If the current user has not joined yet, create and save the UserConversation
        if (currentUserConversation == null) {
            currentUserConversation = new UserConversation();
            currentUserConversation.setUser(user);
            currentUserConversation.setConversation(conversation);
            currentUserConversation.setJoinTime(LocalDateTime.now());
            // Save to database
            currentUserConversation = userConversationRepository.save(currentUserConversation);
        }

        //Repeat the same logic for other users
        UserConversation otherUserConversation = userConversationRepository
                .findByUserAndConversation(otherUser, conversation)
                .orElse(null);

        if (otherUserConversation == null) {
            otherUserConversation = new UserConversation();
            otherUserConversation.setUser(otherUser);
            otherUserConversation.setConversation(conversation);
            otherUserConversation.setJoinTime(LocalDateTime.now());
            otherUserConversation = userConversationRepository.save(otherUserConversation);
        }

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
