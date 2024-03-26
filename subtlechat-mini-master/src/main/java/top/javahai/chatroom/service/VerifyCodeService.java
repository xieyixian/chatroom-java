package top.javahai.chatroom.service;

import java.util.Map;

public interface VerifyCodeService {

    String getVerifyCode();

    void sendVerifyCodeMail(Map<String, String> emailCodeMap);

}
