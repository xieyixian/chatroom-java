package top.team7.chatroom.task;

import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import top.team7.chatroom.entity.MailConstants;
import top.team7.chatroom.entity.MailSendLog;
import top.team7.chatroom.service.MailSendLogService;


import java.util.Date;
import java.util.List;

@Component
public class MailSendTask {
    @Autowired
    MailSendLogService mailSendLogService;
    @Autowired
    RabbitTemplate rabbitTemplate;

    @Scheduled(cron = "0/10 * * * * ?")
    public void mailSendTask() {
        List<MailSendLog> mailSendLogList = mailSendLogService.getMailSendLogsByStatus(MailConstants.DELIVERING);
        mailSendLogList.forEach(mailSendLog -> {
            if (mailSendLog.getCount() > MailConstants.MAX_TRY_COUNT) {
                mailSendLogService.updateMailSendLogStatus(mailSendLog.getMsgId(), MailConstants.FAILURE);
            } else {
                mailSendLogService.updateCount(mailSendLog.getMsgId(), new Date());
                String message=mailSendLogService.getMsgById(mailSendLog.getMsgId());
                rabbitTemplate.convertAndSend(mailSendLog.getExchange(),mailSendLog.getRouteKey(), message, new CorrelationData(mailSendLog.getMsgId()));

            }
        });
    }
}
