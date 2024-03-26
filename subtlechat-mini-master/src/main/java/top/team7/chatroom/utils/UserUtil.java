package top.team7.chatroom.utils;

import org.springframework.security.core.context.SecurityContextHolder;
import top.team7.chatroom.entity.User;


public class UserUtil {
  public static User getCurrentUser(){
    return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
  }
}
