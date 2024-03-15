package top.javahai.chatroom.controller;

import top.javahai.chatroom.dto.JoinConversationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import top.javahai.chatroom.dto.JoinConversationRequest;
import top.javahai.chatroom.entity.Conversation;
import top.javahai.chatroom.entity.UserConversation;
import top.javahai.chatroom.entity.UserMessage;
import top.javahai.chatroom.service.UserChatService;

import java.util.List;

@RestController
@RequestMapping("/userchat")
public class UserChatController {

    private final UserChatService userChatService;

    @Autowired
    public UserChatController(UserChatService userChatService) {
        this.userChatService = userChatService;
    }

    @PostMapping("/conversations")
    public ResponseEntity<Conversation> createConversation(@RequestBody Conversation conversation) {
        Conversation createdConversation = userChatService.createConversation(conversation);
        return ResponseEntity.ok(createdConversation);
    }

    @PostMapping("/conversations/join")
    public ResponseEntity<UserConversation> joinConversation(
            @RequestBody JoinConversationRequest request) {
        System.out.println("Received JoinConversationRequest: " + request);
        System.out.println("Received JoinConversationRequest: " + request.getUsername());
        System.out.println("Received JoinConversationRequest: " + request.getOtherUsername());
        UserConversation userConversation = userChatService.joinConversation(
                request.getUsername(),
                request.getOtherUsername());

        return ResponseEntity.ok(userConversation);
    }

    @GetMapping("/conversations/{conversationId}/messages")
    public ResponseEntity<List<UserMessage>> getMessagesByConversation(
            @PathVariable Long conversationId) {
        List<UserMessage> messages = userChatService.getMessagesByConversation(conversationId);
        return ResponseEntity.ok(messages);
    }

    @GetMapping("/conversations/{conversationId}/messages/paged")
    public ResponseEntity<Page<UserMessage>> getMessagesByConversationPaged(
            @PathVariable Long conversationId,
            Pageable pageable) {
        Page<UserMessage> messagesPage = userChatService.getMessagesByConversation(conversationId, pageable);
        return ResponseEntity.ok(messagesPage);
    }

    @PostMapping("/conversations/{conversationId}/messages")
    public ResponseEntity<UserMessage> sendMessage(
            @PathVariable Long conversationId,
            @RequestParam String username,
            @RequestBody String messageText,@RequestBody int messageTypeId) {
        UserMessage sentMessage = userChatService.sendMessage(username, conversationId, messageText,messageTypeId);
        return ResponseEntity.ok(sentMessage);
    }
}
