package top.team7.chatroom.service;

import java.util.Map;

public interface VerifyCodeService {

    String getVerifyCode();

    void sendVerifyCodeMail(Map<String, String> emailCodeMap);

}
