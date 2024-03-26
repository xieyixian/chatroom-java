package top.team7.chatroom.service.impl;

import org.springframework.stereotype.Service;
import top.team7.chatroom.dao.MailSendLogDao;
import top.team7.chatroom.entity.MailSendLog;
import top.team7.chatroom.service.MailSendLogService;


import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service("mailSendLogService")
public class MailSendLogServiceImpl implements MailSendLogService {
    @Resource
    private MailSendLogDao mailSendLogDao;


    @Override
    public MailSendLog queryById(String msgId) {
        return this.mailSendLogDao.queryById(msgId);
    }


    @Override
    public List<MailSendLog> queryAllByLimit(int offset, int limit) {
        return this.mailSendLogDao.queryAllByLimit(offset, limit);
    }


    @Override
    public MailSendLog insert(MailSendLog mailSendLog) {
        this.mailSendLogDao.insert(mailSendLog);
        return mailSendLog;
    }


    @Override
    public MailSendLog update(MailSendLog mailSendLog) {
        this.mailSendLogDao.update(mailSendLog);
        return this.queryById(mailSendLog.getMsgId());
    }


    @Override
    public boolean deleteById(String msgId) {
        return this.mailSendLogDao.deleteById(msgId) > 0;
    }

    @Override
    public void updateMailSendLogStatus(String msgId, int i) {
        this.mailSendLogDao.updateMailSendLogStatus(msgId,i);
    }

    @Override
    public List<MailSendLog> getMailSendLogsByStatus(Integer delivering) {
        return this.mailSendLogDao.getMailSendLogsByStatus(delivering);
    }

    @Override
    public void updateCount(String msgId, Date date) {
        this.mailSendLogDao.updateCount(msgId,date);
    }

    @Override
    public String getMsgById(String msgId) {
        return  this.mailSendLogDao.getMsgById(msgId);
    }


}
