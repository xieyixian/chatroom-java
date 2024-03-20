package top.javahai.chatroom.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import top.javahai.chatroom.entity.GroupChatRoom;
import top.javahai.chatroom.entity.GroupChatMessage;
import top.javahai.chatroom.entity.User;
import top.javahai.chatroom.service.GroupChatService;
import top.javahai.chatroom.service.UserService;

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

    // 发送群聊消息
    @PostMapping("/send")
    public void sendMessage(@RequestParam Long groupId,
                            @RequestParam String senderUsername,
                            @RequestParam String messageText,
                            @RequestParam int messageTypeId) {
        groupChatService.sendMessage(groupId, senderUsername, messageText, messageTypeId);
    }

    // 获取当前用户加入的群聊的相关信息，包括群聊名称等信息
    @GetMapping("/joined")
    public List<GroupChatRoom> getJoinedGroupChats() {
        // 获取当前用户信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        // 通过用户名获取用户信息
        Optional<User> currentUser = userService.findByUsername(currentUsername);

        if (currentUser.isPresent()) {
            // 获取当前用户加入的群聊的相关信息
            return groupChatService.getJoinedGroupChats(currentUser.get());
        } else {
            // 处理用户不存在的情况，可以抛出异常或者返回空列表，视具体需求而定
            return Collections.emptyList(); // 返回一个空列表作为示例
        }
    }


    // 获取群组中的所有消息
    @GetMapping("/messages")
    public List<GroupChatMessage> getAllMessages(@RequestParam Long groupId) {
        return groupChatService.getAllMessages(groupId);
    }

    // 获取群组中指定发送者的所有消息
    @GetMapping("/messages/sender")
    public List<GroupChatMessage> getMessagesBySender(@RequestParam Long groupId,
                                                      @RequestParam String senderUsername) {
        return groupChatService.getMessagesBySender(groupId, senderUsername);
    }

    // 获取群组中指定类型的所有消息
    @GetMapping("/messages/type")
    public List<GroupChatMessage> getMessagesByType(@RequestParam Long groupId,
                                                    @RequestParam int messageTypeId) {
        return groupChatService.getMessagesByType(groupId, messageTypeId);
    }

    // 加入群组
    @PostMapping("/join")
    public GroupChatRoom joinGroupChat(@RequestParam String groupName,
                                       @RequestParam String groupOwner,
                                       @RequestParam List<String> memberUsernames) {
        // 通过用户名获取用户信息
        Optional<User> owner = userService.findByUsername(groupOwner);
        List<User> members = userService.findByUsernames(memberUsernames);
        // 调用 service 方法加入群组
        return groupChatService.joinGroupChat(groupName, owner, members);
    }
}
