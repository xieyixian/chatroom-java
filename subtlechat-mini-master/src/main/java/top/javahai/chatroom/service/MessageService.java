package top.javahai.chatroom.service;
import org.springframework.beans.factory.annotation.Value;
import top.javahai.chatroom.controller.UserChatController;
import top.javahai.chatroom.entity.GroupMsgContent;
import top.javahai.chatroom.entity.Message;
import top.javahai.chatroom.entity.User;

public interface MessageService {
    public void SendGroupMsg(GroupMsgContent groupMsgContent);

}
