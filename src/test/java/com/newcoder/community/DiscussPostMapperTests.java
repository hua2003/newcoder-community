package com.newcoder.community;

import com.newcoder.community.entity.DiscussPost;
import com.newcoder.community.mapper.DiscussPostMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class DiscussPostMapperTests {
    @Autowired
    private DiscussPostMapper discussPostMapper;
    @Test
    void select() {
        List<DiscussPost> discussPosts = discussPostMapper.selectDiscussPosts(149, 0, 100);
        for (DiscussPost discussPost : discussPosts) {
            System.out.println(discussPost);
        }
        Integer integer = discussPostMapper.selectDiscussPostRows(149);
        System.out.println(integer);
    }
}
