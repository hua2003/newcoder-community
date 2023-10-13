package com.newcoder.community;

import com.newcoder.community.utils.MailUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@SpringBootTest
public class MailTests {
    @Autowired
    private MailUtil mailUtil;

    @Autowired
    private TemplateEngine templateEngine;

    @Test
    void testSendMail() {
        mailUtil.sendMail("2182523056@qq.com", "TEST", "hello world");
    }

    @Test
    void testSendHtmlMail() {
        Context context = new Context();
        context.setVariable("username", "吊毛");
        String content = templateEngine.process("/mail/demo", context);
        mailUtil.sendMail("1126964021@qq.com", "TESTHTML", content);
    }
}
