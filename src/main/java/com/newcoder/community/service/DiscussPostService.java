package com.newcoder.community.service;

import com.newcoder.community.entity.DiscussPost;

import java.util.List;

public interface DiscussPostService {
    List<DiscussPost> findDiscussPosts(Integer userId, Integer offset, Integer limit);
    Integer findDiscussPostRows(Integer userId);
}
