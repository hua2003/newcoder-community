package com.newcoder.community.service;

public interface FollowService {
    void follow(int userId, int entityType, int entityId);
    void unfollow(int userId, int entityType, int entityId);
    long findFolloweeCount(int userId, int entityType);
    long findFollowerCount(int entityType, int entityId);
    boolean findFollowStatus(int userId, int entityType, int entityId);
}
