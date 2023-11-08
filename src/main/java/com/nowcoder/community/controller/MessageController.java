package com.nowcoder.community.controller;

import com.alibaba.fastjson2.JSONObject;
import com.nowcoder.community.entity.Message;
import com.nowcoder.community.entity.Page;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.MessageService;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.utils.CommunityConstant;
import com.nowcoder.community.utils.CommunityUtil;
import com.nowcoder.community.utils.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.HtmlUtils;

import java.util.*;

@Controller
public class MessageController implements CommunityConstant {
    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

    @Autowired
    private HostHolder hostHolder;

    @GetMapping("/letter/list")
    public String getLetterList(Model model, Page page) {
        User user = hostHolder.getUser();
        System.out.println("tyyyyyyyyyes");

        page.setLimit(5);
        page.setPath("/letter/list");
        page.setRows(messageService.findConversationCount(user.getId()));

        List<Message> conversationList = messageService.findConversations(user.getId(), page.getOffset(), page.getLimit());
        List<Map<String, Object>> conversationVO = new ArrayList<>(conversationList.size());
        if (conversationList != null) {
            for (Message message : conversationList) {
                Map<String, Object> map = new HashMap<>();
                map.put("conversation", message);
                map.put("letterCount", messageService.findLetterCount(message.getConversationId()));
                map.put("unreadCount", messageService.findLetterUnreadCount(user.getId(), message.getConversationId()));
                int targetId = message.getFromId() == user.getId() ? message.getToId() : message.getFromId();
                map.put("target", userService.findById(targetId));
                conversationVO.add(map);
            }
        }
        model.addAttribute("conversationVO" ,conversationVO);
        model.addAttribute("letterUnreadCount", messageService.findLetterUnreadCount(user.getId(), null));
        model.addAttribute("noticeUnreadCount", messageService.findNoticeUnreadCount(user.getId(), null));

        return "/site/letter";
    }

    @GetMapping("/letter/list/{conversationId}")
    public String getLetterDetail(@PathVariable("conversationId") String conversationId, Page page, Model model) {
        User user = hostHolder.getUser();

        page.setLimit(5);
        page.setPath("/letter/list/" + conversationId);
        page.setRows(messageService.findLetterCount(conversationId));

        List<Message> letters = messageService.findLetters(conversationId, page.getOffset(), page.getLimit());
        List<Map<String, Object>> letterVO = new ArrayList<>(letters.size());
        for (Message message : letters) {
            Map<String, Object> map = new HashMap<>();
            map.put("letter", message);
            map.put("fromUser", userService.findById(message.getFromId()));
            letterVO.add(map);
        }
        model.addAttribute("letterVO", letterVO);
        String[] s = conversationId.split("_");
        for (String t : s) {
            if (Integer.valueOf(t) != user.getId()) {
                model.addAttribute("target", userService.findById(Integer.valueOf(t)));
                break;
            }
        }

        List<Integer> ids = new ArrayList<>();
        if (letters != null) {
            for (Message letter : letters) {
                if (letter.getToId() == user.getId() && letter.getStatus() == 0) {
                    ids.add(letter.getId());
                }
            }
        }
        if (!ids.isEmpty()) {
            messageService.readMessage(ids, 1);
        }

        return "/site/letter-detail";
    }

    @PostMapping("/letter/send")
    @ResponseBody
    public String sendLetter(String toName, String content) {
        User target = userService.findByName(toName);
        if (target == null) {
            return CommunityUtil.getJSONString(1, "用户不存在！");
        }

        Message message = new Message();
        message.setFromId(hostHolder.getUser().getId());
        message.setToId(target.getId());
        if (message.getFromId() < message.getToId()) {
            message.setConversationId(message.getFromId() + "_" + message.getToId());
        } else {
            message.setConversationId(message.getToId() + "_" + message.getFromId());
        }
        message.setContent(content);
        message.setStatus(0);
        message.setCreateTime(new Date());
        messageService.addMessage(message);

        return CommunityUtil.getJSONString(0);
    }

    @GetMapping("/notice/list")
    public String getNoticePage(Model model) {
        User user = hostHolder.getUser();
        int userId = user.getId();

        Message comment = messageService.findLatestNotice(userId, TOPIC_COMMENT);
        if (comment != null) {
            Map<String, Object> commentVO = new HashMap<>();
            commentVO.put("notice", comment);

            String content = HtmlUtils.htmlUnescape(comment.getContent());
            HashMap data = JSONObject.parseObject(content, HashMap.class);

            commentVO.put("user", userService.findById((Integer) data.get("userId")));
            commentVO.put("entityType", data.get("entityType"));
            commentVO.put("entityId", data.get("entityId"));
            commentVO.put("postId", data.get("postId"));
            commentVO.put("noticeCount", messageService.findNoticeCount(userId, TOPIC_COMMENT));
            commentVO.put("unreadCount", messageService.findNoticeUnreadCount(userId, TOPIC_COMMENT));
            model.addAttribute("commentVO", commentVO);
        }

        Message like = messageService.findLatestNotice(userId, TOPIC_LIKE);
        if (like != null) {
            Map<String, Object> likeVO = new HashMap<>();
            likeVO.put("notice", like);
            String content = HtmlUtils.htmlUnescape(like.getContent());
            HashMap data = JSONObject.parseObject(content, HashMap.class);

            likeVO.put("user", userService.findById((Integer) data.get("userId")));
            likeVO.put("entityType", data.get("entityType"));
            likeVO.put("entityId", data.get("entityId"));
            likeVO.put("postId", data.get("postId"));
            likeVO.put("noticeCount", messageService.findNoticeCount(userId, TOPIC_LIKE));
            likeVO.put("unreadCount", messageService.findNoticeUnreadCount(userId, TOPIC_LIKE));
            model.addAttribute("likeVO", likeVO);
        }

        Message follow = messageService.findLatestNotice(userId, TOPIC_FOLLOW);
        if (follow != null) {
            Map<String, Object> followVO = new HashMap<>();
            followVO.put("notice", follow);

            String content = HtmlUtils.htmlUnescape(follow.getContent());
            HashMap data = JSONObject.parseObject(content, HashMap.class);

            followVO.put("user", userService.findById((Integer) data.get("userId")));
            followVO.put("entityType", data.get("entityType"));
            followVO.put("entityId", data.get("entityId"));
            followVO.put("postId", data.get("postId"));
            followVO.put("noticeCount", messageService.findNoticeCount(userId, TOPIC_FOLLOW));
            followVO.put("unreadCount", messageService.findNoticeUnreadCount(userId, TOPIC_FOLLOW));
            model.addAttribute("followVO", followVO);
        }

        return "/site/notice";
    }

    @GetMapping("/notice/detail/{topic}")
    public String getNoticeDetail(@PathVariable("topic") String topic, Page page, Model model) {
        User user = hostHolder.getUser();

        page.setLimit(5);
        page.setPath("/notice/detail/" + topic);
        page.setRows(messageService.findNoticeCount(user.getId(), topic));

        List<Message> notices = messageService.findNotices(user.getId(), topic, page.getOffset(), page.getLimit());
        List<Map<String, Object>> noticesVO = new ArrayList<>(notices.size());
        for (Message notice : notices) {
            Map<String, Object> map = new HashMap<>();
            map.put("notice", notice);

            String content = HtmlUtils.htmlUnescape(notice.getContent());
            HashMap data = JSONObject.parseObject(content, HashMap.class);
            map.put("user", userService.findById((Integer) data.get("userId")));
            map.put("entityType", data.get("entityType"));
            map.put("entityId", data.get("entityId"));
            map.put("postId", data.get("postId"));
            map.put("fromUser", userService.findById(notice.getFromId()));
            noticesVO.add(map);
        }
        model.addAttribute("noticeVO", noticesVO);

        List<Integer> ids = new ArrayList<>();
        if (notices != null) {
            for (Message notice : notices) {
                if (notice.getToId() == user.getId() && notice.getStatus() == 0) {
                    ids.add(notice.getId());
                }
            }
        }
        if (!ids.isEmpty()) {
            messageService.readMessage(ids, 1);
        }

        return "/site/notice-detail";
    }
}
