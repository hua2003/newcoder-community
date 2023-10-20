package com.newcoder.community.service.impl;

import com.newcoder.community.entity.Comment;
import com.newcoder.community.mapper.CommentMapper;
import com.newcoder.community.mapper.DiscussPostMapper;
import com.newcoder.community.service.CommentService;
import com.newcoder.community.utils.CommunityConstant;
import com.newcoder.community.utils.SensitiveFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService, CommunityConstant {
    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private DiscussPostMapper discussPostMapper;

    @Autowired
    private SensitiveFilter sensitiveFilter;

    @Override
    public List<Comment> findCommentsByEntity(int entityType, int entityId, int offset, int limit) {
        return commentMapper.selectCommentByEntity(entityType, entityId, offset, limit);
    }

    @Override
    public int findCommentCount(int entityType, int entityId) {
        return commentMapper.selectCountByEntity(entityType, entityId);
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public int insertComment(Comment comment) {
        if (comment == null) {
            throw new IllegalArgumentException("评论不能为空");
        }

        comment.setContent(HtmlUtils.htmlEscape(comment.getContent()));
        comment.setContent(sensitiveFilter.filter(comment.getContent()));
        commentMapper.insertComment(comment);

        if (comment.getEntityType() == ENTITY_TYPE_POST) {
            int count = commentMapper.selectCountByEntity(comment.getEntityType(), comment.getEntityId());
            discussPostMapper.updateCommentCount(comment.getEntityId(), count);
        }

        return 0;
    }

    @Override
    public Comment findCommentById(int commentId) {
        return commentMapper.selectCommentById(commentId);
    }
}
