package com.newcoder.community.service;

public interface LikeService {
    void like(int userId, int entityType, int entityId, int entityUserId);
    long findLikeCount(int entityType, int entityId);
    int findLikeStatus(int userId, int entityType, int entityId);
    int findUserLikeCount(int userId);
}
