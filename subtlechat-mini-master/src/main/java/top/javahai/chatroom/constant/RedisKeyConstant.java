package top.javahai.chatroom.constant;

/**
 * 缓存key
 */
public interface RedisKeyConstant {

    //在线状态set集合
    public static final String USER_STAT_SET_KEY = "userStatSet";

    //用户在线状态续命key
    public static final String USER_CONTINUE_LIFE_KEY = "userContinueLife_%d";

    //私聊key
    public static final String PRIVATE_MSG_LIMIT_KEY = "privateLimit_%d_%d";

    //机器人key
    public static final String ROBOT_MSG_LIMIT_KEY = "robotLimit_%d";

    //群聊限流key
    public static final String GROUP_MSG_LIMIT_KEY = "groupLimit_%d";

    //群聊消息key
    public static final String GROUP_MSG_LIST_KEY = "groupMsgList_%s";
}
