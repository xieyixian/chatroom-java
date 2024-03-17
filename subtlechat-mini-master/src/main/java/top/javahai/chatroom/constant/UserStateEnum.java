package top.javahai.chatroom.constant;

import lombok.Getter;

/**
 * 用户状态
 */
@Getter
public enum UserStateEnum {
    ONLINE(1,"在线"),
    OFFLINE(2,"离线"),
    LOGOFF(3,"注销");

    private int code;
    private String codeStr;

    UserStateEnum(int code, String codeStr) {
        this.code = code;
        this.codeStr = codeStr;
    }


}
