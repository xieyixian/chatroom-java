package top.team7.chatroom.constant;

public interface RedisKeyConstant {


    public static final String USER_STAT_SET_KEY = "userStatSet";

    public static final String USER_CONTINUE_LIFE_KEY = "userContinueLife_%d";

    public static final String PRIVATE_MSG_LIMIT_KEY = "privateLimit_%d_%d";

    public static final String ROBOT_MSG_LIMIT_KEY = "robotLimit_%d";

    public static final String GROUP_MSG_LIMIT_KEY = "groupLimit_%d";

    public static final String GROUP_MSG_LIST_KEY = "groupMsgList_%s";
}
