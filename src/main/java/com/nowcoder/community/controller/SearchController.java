package com.nowcoder.community.controller;

import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.entity.Page;
import com.nowcoder.community.service.ElasticsearchService;
import com.nowcoder.community.service.LikeService;
import com.nowcoder.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class SearchController {
    @Autowired
    private ElasticsearchService elasticsearchService;

    @Autowired
    private UserService userService;

    @Autowired
    private LikeService likeService;

    @GetMapping("/search")
    public String getSearchPage(String keyword, Page page, Model model) {
        org.springframework.data.domain.Page<DiscussPost> searchDiscussPosts = elasticsearchService.searchDiscussPost(keyword, page.getCurrent() - 1, page.getLimit());

        List<Map<String, Object>> discussPosts = new ArrayList<>();
        if (searchDiscussPosts != null) {
            for (DiscussPost discussPost : searchDiscussPosts) {
                Map<String, Object> map = new HashMap<>();
                map.put("post", discussPost);
                map.put("user", userService.findById(discussPost.getUserId()));
                map.put("likeCount", likeService.findLikeCount(discussPost.getType(), discussPost.getId()));
                discussPosts.add(map);
            }
        }
        model.addAttribute("discussPosts", discussPosts);
        model.addAttribute("keyword", keyword);

        page.setRows(searchDiscussPosts == null ? 0 : (int) searchDiscussPosts.getTotalElements());
        page.setPath("/search?keyword=" + keyword);

        return "/site/search";
    }
}
