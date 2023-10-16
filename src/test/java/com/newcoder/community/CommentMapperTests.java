package com.newcoder.community;

import com.newcoder.community.entity.Comment;
import com.newcoder.community.mapper.CommentMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

@SpringBootTest
public class CommentMapperTests {
    @Autowired
    private CommentMapper commentMapper;

    @Test
    void testInsert() {
        Comment comment = new Comment();
        comment.setUserId(150);
        comment.setEntityId(1);
        comment.setEntityType(1);
        comment.setStatus(0);
        comment.setCreateTime(new Date());
        commentMapper.insertComment(comment);
        System.out.println(comment.getId());
    }
}
