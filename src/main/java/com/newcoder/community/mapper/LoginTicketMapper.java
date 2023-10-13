package com.newcoder.community.mapper;

import com.newcoder.community.entity.LoginTicket;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LoginTicketMapper {
    Integer insertLoginTicket(LoginTicket loginTicket);
    LoginTicket selectLoginTicketByTicket(String ticket);
    Integer updateLoginTicketStatus(String ticket, Integer status);
}
