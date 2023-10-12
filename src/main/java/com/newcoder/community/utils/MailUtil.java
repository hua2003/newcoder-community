package com.newcoder.community.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Component
public class MailUtil {
    private static final Logger logger = LoggerFactory.getLogger(MailUtil.class);
    @Autowired
    private JavaMailSender mailSender;

    /**
     * 发送邮件对象
     */
    @Value("${spring.mail.username}")
    private String from;

    /**
     *
     * @param to 接收邮件对象
     * @param subject 邮件标题
     * @param content 邮件内容
     */
    public void sendMail(String to, String subject, String content) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);
            mailSender.send(helper.getMimeMessage());
        } catch (MessagingException e) {
            logger.error("发送普通邮件失败:" + e.getMessage());
        }
    }
}
