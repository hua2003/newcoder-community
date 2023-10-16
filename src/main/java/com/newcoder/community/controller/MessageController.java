package com.newcoder.community.controller;

import com.newcoder.community.entity.Comment;
import com.newcoder.community.entity.Message;
import com.newcoder.community.entity.Page;
import com.newcoder.community.entity.User;
import com.newcoder.community.service.MessageService;
import com.newcoder.community.service.UserService;
import com.newcoder.community.utils.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class MessageController {
    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

    @Autowired
    private HostHolder hostHolder;

    @GetMapping("/letter/list")
    public String getLetterList(Model model, Page page) {
        User user = hostHolder.getUser();

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

        return "/site/letter-detail";
    }
}
