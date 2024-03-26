package top.team7.chatroom.dao;

import org.apache.ibatis.annotations.Param;
import top.team7.chatroom.entity.MailSendLog;


import java.util.Date;
import java.util.List;

public interface MailSendLogDao {

    MailSendLog queryById(String msgId);

    List<MailSendLog> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);

    List<MailSendLog> queryAll(MailSendLog mailSendLog);

    int insert(MailSendLog mailSendLog);

    int update(MailSendLog mailSendLog);

    int deleteById(String msgId);

    void updateMailSendLogStatus(@Param("msgId") String msgId,@Param("status") int i);

    List<MailSendLog> getMailSendLogsByStatus(@Param("delivering") Integer delivering);

    void updateCount(@Param("msgId") String msgId,@Param("date") Date date);

    String getMsgById(@Param("msgId") String msgId);
}
