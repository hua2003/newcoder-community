package com.newcoder.community.service;

import com.newcoder.community.entity.User;

import java.util.Map;

public interface UserService {
    User findById(Integer id);
    Map<String, Object> register(User user);

    int activation(Integer userId, String code);
}
