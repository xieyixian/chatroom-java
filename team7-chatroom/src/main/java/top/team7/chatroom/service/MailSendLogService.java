package top.team7.chatroom.service;



import top.team7.chatroom.entity.MailSendLog;

import java.util.Date;
import java.util.List;


public interface MailSendLogService {


    MailSendLog queryById(String msgId);


    List<MailSendLog> queryAllByLimit(int offset, int limit);


    MailSendLog insert(MailSendLog mailSendLog);


    MailSendLog update(MailSendLog mailSendLog);

    boolean deleteById(String msgId);

    void updateMailSendLogStatus(String msgId, int i);

    List<MailSendLog> getMailSendLogsByStatus(Integer delivering);

    void updateCount(String msgId, Date date);

    String getMsgById(String msgId);
}
