package top.team7.chatroom.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import top.team7.chatroom.entity.User;
import top.team7.chatroom.service.UserService;
import top.team7.chatroom.entity.GroupChatRoom;
import top.team7.chatroom.entity.GroupChatMessage;
import top.team7.chatroom.service.GroupChatService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/groupchat")
public class GroupChatController {

    private final GroupChatService groupChatService;
    private final UserService userService;

    @Autowired
    public GroupChatController(GroupChatService groupChatService, UserService userService) {
        this.groupChatService = groupChatService;
        this.userService = userService;
    }

    // Send group chat message
    @PostMapping("/send")
    public void sendMessage(@RequestParam Long groupId,
                            @RequestParam String senderUsername,
                            @RequestParam String messageText,
                            @RequestParam int messageTypeId) {
        groupChatService.sendMessage(groupId, senderUsername, messageText, messageTypeId);
    }


    @GetMapping("/joined")
    public List<GroupChatRoom> getJoinedGroupChats() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        Optional<User> currentUser = userService.findByUsername(currentUsername);

        if (currentUser.isPresent()) {
            return groupChatService.getJoinedGroupChats(currentUser.get());
        } else {

            return Collections.emptyList();
        }
    }



    @GetMapping("/messages")
    public List<GroupChatMessage> getAllMessages(@RequestParam Long groupId) {
        return groupChatService.getAllMessages(groupId);
    }

    @GetMapping("/messages/sender")
    public List<GroupChatMessage> getMessagesBySender(@RequestParam Long groupId,
                                                      @RequestParam String senderUsername) {
        return groupChatService.getMessagesBySender(groupId, senderUsername);
    }


    @GetMapping("/messages/type")
    public List<GroupChatMessage> getMessagesByType(@RequestParam Long groupId,
                                                    @RequestParam int messageTypeId) {
        return groupChatService.getMessagesByType(groupId, messageTypeId);
    }

    @PostMapping("/join")
    public GroupChatRoom joinGroupChat(@RequestParam String groupName,
                                       @RequestParam String groupOwner,
                                       @RequestParam List<String> memberUsernames) {
        Optional<User> owner = userService.findByUsername(groupOwner);
        List<User> members = userService.findByUsernames(memberUsernames);
        return groupChatService.joinGroupChat(groupName, owner, members);
    }
}
