package com.newcoder.community.utils;

import com.newcoder.community.entity.User;
import org.springframework.stereotype.Component;

@Component
public class HostHolder {
    private ThreadLocal<User> local = new ThreadLocal<User>();

    public void setUser(User user) {
        local.set(user);
    }

    public User getUser() {
        return local.get();
    }

    public void clear() {
        local.remove();
    }
}
