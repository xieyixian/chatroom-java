package top.team7.chatroom.constant;

import lombok.Getter;


@Getter
public enum UserStateEnum {
    ONLINE(1,"online"),
    OFFLINE(2,"Offline"),
    LOGOFF(3,"Log out");

    private int code;
    private String codeStr;

    UserStateEnum(int code, String codeStr) {
        this.code = code;
        this.codeStr = codeStr;
    }


}
