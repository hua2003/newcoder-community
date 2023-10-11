package com.newcoder.community.mapper;

import com.newcoder.community.entity.DiscussPost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Mapper
public interface DiscussPostMapper {
    List<DiscussPost> selectDiscussPosts(
            @Param("userId") Integer userId,
            @Param("offset") Integer offset,
            @Param("limit") Integer limit);
    Integer selectDiscussPostRows(@Param("userId") Integer userId);
}
