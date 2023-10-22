package com.nowcoder.community;

import com.nowcoder.community.entity.LoginTicket;
import com.nowcoder.community.mapper.LoginTicketMapper;
import com.nowcoder.community.utils.CommunityUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

@SpringBootTest
public class LoginTicketMapperTests {
    @Autowired
    private LoginTicketMapper loginTicketMapper;

    @Test
    void testInsert() {
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(149);
        loginTicket.setTicket(CommunityUtil.generateUUID());
        loginTicket.setStatus(0);
        loginTicket.setExpired(new Date(System.currentTimeMillis() + 30 * 60 * 1000));
        loginTicketMapper.insertLoginTicket(loginTicket);
    }

    @Test
    void testSelect() {
        String ticket = "f6fa5ddab0784ff2bf578bfb64b66c19";
        LoginTicket loginTicket = loginTicketMapper.selectLoginTicketByTicket(ticket);
        System.out.println(loginTicket);
    }

    @Test
    void testUpdat() {
        String ticket = "f6fa5ddab0784ff2bf578bfb64b66c19";
        Integer status = 1;
        Integer integer = loginTicketMapper.updateLoginTicketStatus(ticket, status);
    }
}
