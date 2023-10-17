package com.newcoder.community.controller;

import com.newcoder.community.entity.User;
import com.newcoder.community.service.LikeService;
import com.newcoder.community.utils.CommunityUtil;
import com.newcoder.community.utils.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
public class LikeController {
    @Autowired
    private LikeService likeService;

    @Autowired
    private HostHolder hostHolder;

    @PostMapping("/like")
    @ResponseBody
    public String like(int entityType, int entityId, int entityUserId) {
        User user = hostHolder.getUser();
        likeService.like(user.getId(), entityType, entityId, entityUserId);

        Map<String, Object> map = new HashMap<>();
        map.put("likeCount", likeService.findLikeCount(entityType, entityId));
        map.put("likeStatus", likeService.findLikeStatus(user.getId(), entityType ,entityId));
        return CommunityUtil.getJSONString(0, null, map);
    }
}
