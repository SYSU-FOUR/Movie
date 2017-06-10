package lab.io.rush.moana.service.impl;

import java.io.IOException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import lab.io.rush.moana.entity.Email;
import lab.io.rush.moana.service.MailService;
@Service("mailService")
public class MailServiceImpl implements MailService {  
	@Autowired
    private JavaMailSender mailSender;
	
	@Autowired
    private TaskExecutor taskExecutor;
  
    private Log log = LogFactory.getLog(getClass());  
    private StringBuffer message = new StringBuffer();  
  
    @Override  
    public void sendMail(Email email) throws MessagingException, IOException{  
        /*if (email.getAddress().length > 5) {// �ռ��˴���5��ʱ�������첽���� 
            sendMailByAsynchronousMode(email); 
            this.message.append("�ռ��˹��࣬���ڲ����첽��ʽ����...<br/>"); 
        } else {*/  
        sendMailBySynchronizationMode(email);  
        //this.message.append("����ͬ����ʽ�����ʼ�...<br/>");  
        //}  
    }  
  
      
    @Override  
    public void sendMailByAsynchronousMode(final Email email) {  
        taskExecutor.execute(new Runnable() {  
            public void run() {  
                try {  
                    sendMailBySynchronizationMode(email);  
                } catch (Exception e) {  
                    log.info(e);  
                }  
            }  
        });  
    }  
  
    @Override  
    public void sendMailBySynchronizationMode(Email email)  
            throws MessagingException, IOException {  
        MimeMessage mime = mailSender.createMimeMessage();  
        MimeMessageHelper helper = new MimeMessageHelper(mime, true, "utf-8");  
        helper.setFrom(email.getFromAddress());// ������  
        helper.setTo(email.getToAddress());// �ռ���  
        helper.setReplyTo(email.getFromAddress());// �ظ���  
        helper.setSubject(email.getSubject());// �ʼ�����  
        helper.setText(email.getContent(), true);// true��ʾ�趨html��ʽ  
        mailSender.send(mime);  
    }  
  
      
      
      
    public JavaMailSender getMailSender() {  
        return mailSender;  
    }  
  
    public void setMailSender(JavaMailSender mailSender) {  
        this.mailSender = mailSender;  
    }  
  
    public TaskExecutor getTaskExecutor() {  
        return taskExecutor;  
    }  
  
    public void setTaskExecutor(TaskExecutor taskExecutor) {  
        this.taskExecutor = taskExecutor;  
    }  
  
    public Log getLog() {  
        return log;  
    }  
  
    public void setLog(Log log) {  
        this.log = log;  
    }  
  
    public StringBuffer getMessage() {  
        return message;  
    }  
  
    public void setMessage(StringBuffer message) {  
        this.message = message;  
    }  
  
} 