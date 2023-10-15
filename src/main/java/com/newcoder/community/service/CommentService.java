package com.newcoder.community.service;

import com.newcoder.community.entity.Comment;

import java.util.List;

public interface CommentService {
    List<Comment> findCommentsByEntity(int entityType, int entityId, int offset, int limit);
    int findCommentCount(int entityType, int entityId);
    int insertComment(Comment comment);
}

