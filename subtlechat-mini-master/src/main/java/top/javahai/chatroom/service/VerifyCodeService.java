package top.javahai.chatroom.service;

import java.util.Map;

/**
 * @author Hai
 * @date 2020/10/2 - 23:27
 */
public interface VerifyCodeService {

    String getVerifyCode();

    void sendVerifyCodeMail(Map<String, String> emailCodeMap);

}
