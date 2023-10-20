package com.newcoder.community.controller;

import com.newcoder.community.entity.Event;
import com.newcoder.community.entity.User;
import com.newcoder.community.event.EventProducer;
import com.newcoder.community.service.LikeService;
import com.newcoder.community.utils.CommunityConstant;
import com.newcoder.community.utils.CommunityUtil;
import com.newcoder.community.utils.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
public class LikeController implements CommunityConstant {
    @Autowired
    private LikeService likeService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private EventProducer eventProducer;

    @PostMapping("/like")
    @ResponseBody
    public String like(int entityType, int entityId, int entityUserId, int postId) {
        User user = hostHolder.getUser();
        likeService.like(user.getId(), entityType, entityId, entityUserId);

        Map<String, Object> map = new HashMap<>();
        long likeCount = likeService.findLikeCount(entityType, entityId);
        int likeStatus = likeService.findLikeStatus(user.getId(), entityType, entityId);
        map.put("likeCount", likeCount);
        map.put("likeStatus", likeStatus);

        if (likeStatus == 1) {
            Event event = new Event()
                    .setTopic(TOPIC_LIKE)
                    .setEntityType(entityType)
                    .setEntityId(entityId)
                    .setEntityUserId(entityUserId)
                    .setData("postId", postId);
            eventProducer.fireEvent(event);
        }

        return CommunityUtil.getJSONString(0, null, map);
    }
}
