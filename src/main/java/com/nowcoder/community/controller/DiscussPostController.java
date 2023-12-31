package com.nowcoder.community.controller;

import com.nowcoder.community.entity.*;
import com.nowcoder.community.event.EventProducer;
import com.nowcoder.community.service.CommentService;
import com.nowcoder.community.service.DiscussPostService;
import com.nowcoder.community.service.LikeService;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.utils.CommunityConstant;
import com.nowcoder.community.utils.CommunityUtil;
import com.nowcoder.community.utils.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/discuss")
public class DiscussPostController implements CommunityConstant {
    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private UserService userService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private LikeService likeService;

    @Autowired
    private EventProducer eventProducer;

    @ResponseBody
    @PostMapping("/add")
    public String addDiscussPost(String title, String content) {
        User user = hostHolder.getUser();
        if (user == null) {
            return CommunityUtil.getJSONString(403, "用户未登录");
        }

        DiscussPost discussPost = new DiscussPost();
        discussPost.setTitle(title);
        discussPost.setContent(content);
        discussPost.setUserId(user.getId());
        discussPost.setCreateTime(new Date());
        discussPost.setType(0);
        discussPost.setStatus(0);
        discussPost.setCommentCount(0);
        discussPost.setScore(0.0);
        discussPostService.insertDiscussPost(discussPost);

        Event event = new Event()
                .setTopic(TOPIC_PUBLISH)
                .setUserId(user.getId())
                .setEntityType(ENTITY_TYPE_POST)
                .setEntityId(discussPost.getId());
        eventProducer.fireEvent(event);

        return CommunityUtil.getJSONString(0, "发布成功！");
    }

    @GetMapping("/detail/{id}")
    public String getDiscussPostDetail(Model model, @PathVariable("id") Integer id, Page page) {
        DiscussPost discussPost = discussPostService.selectDiscussPostById(id);
        User user = userService.findById(discussPost.getUserId());
        model.addAttribute("post", discussPost);
        model.addAttribute("user", user);

        model.addAttribute("likeCount", likeService.findLikeCount(ENTITY_TYPE_POST, id));
        model.addAttribute("likeStatus", hostHolder.getUser() == null ? 0 : likeService.findLikeStatus(hostHolder.getUser().getId(), ENTITY_TYPE_POST, discussPost.getId()));

        page.setLimit(5);
        page.setPath("/discuss/detail/" + id);
        page.setRows(discussPost.getCommentCount());

        List<Comment> list = commentService.findCommentsByEntity(ENTITY_TYPE_POST, id, page.getOffset(), page.getLimit());
        List<Map<String, Object>> commentVOList = new ArrayList<>(list.size());
        for (Comment comment : list) {
            Map<String, Object> commentVO = new HashMap<>();
            commentVO.put("comment", comment);
            commentVO.put("user", userService.findById(comment.getUserId()));

            commentVO.put("likeCount", likeService.findLikeCount(ENTITY_TYPE_COMMENT, comment.getId()));
            commentVO.put("likeStatus", likeService.findLikeStatus(hostHolder.getUser() == null ? 0 : hostHolder.getUser().getId(), ENTITY_TYPE_COMMENT, comment.getId()));

            List<Comment> replyList = commentService.findCommentsByEntity(ENTITY_TYPE_COMMENT, comment.getId(), 0, Integer.MAX_VALUE);
            List<Map<String, Object>> replyVOList = new ArrayList<>(replyList.size());
            if (replyList != null) {
                for (Comment reply : replyList) {
                    Map<String, Object> replyVO = new HashMap<>();
                    replyVO.put("reply", reply);
                    replyVO.put("user", userService.findById(reply.getUserId()));
                    User target = reply.getTargetId() == 0 ? null : userService.findById(reply.getTargetId());
                    replyVO.put("target", target);
                    replyVOList.add(replyVO);

                    replyVO.put("likeCount", likeService.findLikeCount(ENTITY_TYPE_COMMENT, reply.getId()));
                    replyVO.put("likeStatus", likeService.findLikeStatus(hostHolder.getUser() == null ? 0 : hostHolder.getUser().getId(), ENTITY_TYPE_COMMENT, reply.getEntityId()));
                }
            }
            commentVO.put("replys", replyVOList);
            int replyCount = commentService.findCommentCount(ENTITY_TYPE_COMMENT, comment.getId());
            commentVO.put("replyCount", replyCount);
            commentVOList.add(commentVO);
        }
        model.addAttribute("comments", commentVOList);

        return "/site/discuss-detail";
    }

    /**
     * 置顶
     * @param id
     * @return
     */
    @PostMapping("/top")
    @ResponseBody
    public String setTop(int id) {
        discussPostService.updateType(id, 1);

        Event event = new Event()
                .setTopic(TOPIC_PUBLISH)
                .setUserId(hostHolder.getUser().getId())
                .setEntityType(ENTITY_TYPE_POST)
                .setEntityId(id);
        eventProducer.fireEvent(event);

        return CommunityUtil.getJSONString(0);
    }

    /**
     * 加精
     * @param id
     * @return
     */
    @PostMapping("/wonderful")
    @ResponseBody
    public String setWonderful(int id) {
        discussPostService.updateStatus(id, 1);

        Event event = new Event()
                .setTopic(TOPIC_PUBLISH)
                .setUserId(hostHolder.getUser().getId())
                .setEntityType(ENTITY_TYPE_POST)
                .setEntityId(id);
        eventProducer.fireEvent(event);

        return CommunityUtil.getJSONString(0);
    }

    /**
     * 删除
     * @param id
     * @return
     */
    @PostMapping("/delete")
    @ResponseBody
    public String setDelete(int id) {
        discussPostService.updateStatus(id, 2);

        Event event = new Event()
                .setTopic(TOPIC_DELETE)
                .setUserId(hostHolder.getUser().getId())
                .setEntityType(ENTITY_TYPE_POST)
                .setEntityId(id);
        eventProducer.fireEvent(event);

        return CommunityUtil.getJSONString(0);
    }
}
