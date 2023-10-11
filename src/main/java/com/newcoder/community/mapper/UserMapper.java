package com.newcoder.community.mapper;

import com.newcoder.community.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    User selectById(Integer id);
    User selectByName(String name);
    User selectByEmail(String email);
    int insertUser(User user);
    int updateStatus(Integer id, Integer status);
    int updateHeader(Integer id, String headerUrl);
    int updatePassword(Integer id, String password);
}
