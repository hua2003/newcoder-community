package com.newcoder.community.service.impl;

import com.newcoder.community.entity.User;
import com.newcoder.community.mapper.UserMapper;
import com.newcoder.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public User findById(Integer id) {
        return userMapper.selectById(id);
    }
}
