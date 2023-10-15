package com.newcoder.community.service;

import com.newcoder.community.entity.DiscussPost;
import com.newcoder.community.entity.User;

import java.util.Map;

public interface UserService {
    User findById(int id);
    Map<String, Object> register(User user);
    int activation(int userId, String code);
    Map<String, Object> login(String username, String password, long expired);
    void logout(String ticket);
    void updateHeader(int userId, String headerUrl);
}
