package com.newcoder.community.mapper;

import com.newcoder.community.entity.LoginTicket;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LoginTicketMapper {
    int insertLoginTicket(LoginTicket loginTicket);
    LoginTicket selectLoginTicketByTicket(String ticket);
    int updateLoginTicketStatus(String ticket, int status);
}
