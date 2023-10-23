package com.nowcoder.community.service;

import com.nowcoder.community.entity.User;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Map;

public interface UserService {
    User findById(int id);
    Map<String, Object> register(User user);
    int activation(int userId, String code);
    Map<String, Object> login(String username, String password, long expired);
    void logout(String ticket);
    int updateHeader(int userId, String headerUrl);
    User findByName(String name);
    User getCache(int userId);
    User initCache(int userId);
    void clearCache(int userId);
    Collection<? extends GrantedAuthority> getAuthorities(int userId);
}
