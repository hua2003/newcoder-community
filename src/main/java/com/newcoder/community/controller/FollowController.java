package com.newcoder.community.controller;

import com.newcoder.community.entity.Page;
import com.newcoder.community.entity.User;
import com.newcoder.community.service.FollowService;
import com.newcoder.community.service.UserService;
import com.newcoder.community.utils.CommunityConstant;
import com.newcoder.community.utils.CommunityUtil;
import com.newcoder.community.utils.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
public class FollowController implements CommunityConstant {
    @Autowired
    private FollowService followService;

    @Autowired
    private HostHolder hostHolder;
    
    @Autowired
    private UserService userService;

    @PostMapping("/follow")
    @ResponseBody
    public String follow(int entityType, int entityId) {
        User user = hostHolder.getUser();
        followService.follow(user.getId(), entityType, entityId);
        return CommunityUtil.getJSONString(0, "已关注！");
    }

    @PostMapping("/unfollow")
    @ResponseBody
    public String unfollow(int entityType, int entityId) {
        User user = hostHolder.getUser();
        followService.unfollow(user.getId(), entityType, entityId);
        return CommunityUtil.getJSONString(0, "已取消关注");
    }

    @GetMapping("/followees/{userId}")
    public String getFolloweesPage(@PathVariable("userId") int userId, Page page, Model model) {
        User user = userService.findById(userId);
        if (user == null) {
            throw new IllegalArgumentException("用户不存在！");
        }
        
        model.addAttribute("user", user);
        page.setLimit(5);
        page.setPath("/followees/" + userId);
        page.setRows((int) followService.findFolloweeCount(userId, ENTITY_TYPE_USER));

        List<Map<String, Object>> followees = followService.findFollowees(userId, page.getOffset(), page.getLimit());
        for (Map<String, Object> followee : followees) {
            boolean hasFollowed = false;
            if (hostHolder.getUser() != null) {
                User targetUser = (User) followee.get("user");
                hasFollowed = followService.findFollowStatus(hostHolder.getUser().getId(), ENTITY_TYPE_USER, targetUser.getId());
            }
            followee.put("hasFollowed", hasFollowed);
        }
        model.addAttribute("followees", followees);
        return "/site/followee";
    }

    @GetMapping("/followers/{userId}")
    public String getFollowersPage(@PathVariable("userId") int userId, Page page, Model model) {
        User user = userService.findById(userId);
        if (user == null) {
            throw new IllegalArgumentException("用户不存在！");
        }

        model.addAttribute("user", user);
        page.setLimit(5);
        page.setPath("/followers/" + userId);
        page.setRows((int) followService.findFollowerCount(userId, ENTITY_TYPE_USER));

        List<Map<String, Object>> followers = followService.findFollowers(userId, page.getOffset(), page.getLimit());
        for (Map<String, Object> follower : followers) {
            boolean hasFollowed = false;
            if (hostHolder.getUser() != null) {
                User targetUser = (User) follower.get("user");
                hasFollowed = followService.findFollowStatus(hostHolder.getUser().getId(), ENTITY_TYPE_USER, targetUser.getId());
            }
            follower.put("hasFollowed", hasFollowed);
        }
        model.addAttribute("followers", followers);
        return "/site/follower";
    }
}
