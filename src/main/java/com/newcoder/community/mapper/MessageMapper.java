package com.newcoder.community.mapper;

import com.newcoder.community.entity.Message;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MessageMapper {
    // 查询当前用户的会话列表，针对每个会话只查询最新的一条消息
    List<Message> selectConversations(int userId, int offset, int limit);
    // 查询当前用户的会话列表的数量
    int selectConversationCount(int userId);
    // 查询某个会话的私信列表
    List<Message> selectLetters(String conversationId, int offset, int limit);
    // 查询某个会话的私信个数
    int selectLetterCount(String conversationId);
    // 查询未读的私信个数
    int selectLetterUnreadCount(int userId, String conversationId);
}
