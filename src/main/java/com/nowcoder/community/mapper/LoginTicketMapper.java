package com.nowcoder.community.mapper;

import com.nowcoder.community.entity.LoginTicket;
import org.apache.ibatis.annotations.Mapper;

@Mapper
@Deprecated
public interface LoginTicketMapper {
    int insertLoginTicket(LoginTicket loginTicket);
    LoginTicket selectLoginTicketByTicket(String ticket);
    int updateLoginTicketStatus(String ticket, int status);
}
