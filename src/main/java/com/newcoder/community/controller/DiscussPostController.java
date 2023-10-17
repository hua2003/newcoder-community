package com.newcoder.community.controller;

import com.newcoder.community.entity.Comment;
import com.newcoder.community.entity.DiscussPost;
import com.newcoder.community.entity.Page;
import com.newcoder.community.entity.User;
import com.newcoder.community.service.CommentService;
import com.newcoder.community.service.DiscussPostService;
import com.newcoder.community.service.LikeService;
import com.newcoder.community.service.UserService;
import com.newcoder.community.utils.CommunityConstant;
import com.newcoder.community.utils.CommunityUtil;
import com.newcoder.community.utils.HostHolder;
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
}
