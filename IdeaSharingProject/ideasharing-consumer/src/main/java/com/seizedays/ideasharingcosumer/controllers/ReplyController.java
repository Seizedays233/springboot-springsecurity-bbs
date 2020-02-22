package com.seizedays.ideasharingcosumer.controllers;


import com.alibaba.dubbo.config.annotation.Reference;
import com.seizedays.beans.IdeaUser;
import com.seizedays.beans.Post;
import com.seizedays.beans.Reply;
import com.seizedays.services.PostService;
import com.seizedays.services.ReplyService;
import com.seizedays.services.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

@Controller
public class ReplyController {


    @Reference
    private ReplyService replyService;

    @Reference
    private UserService userService;

    @Reference
    private PostService postService;

    @RequestMapping(value = "/reply.do", method = RequestMethod.POST)
    @ResponseBody
    public Integer reply(@RequestParam("repBody") String repBody, @RequestParam("pid") Long pid, @AuthenticationPrincipal UserDetails user) {
        Reply reply = new Reply();
        Post post = postService.selectPostByPid(pid);
        IdeaUser ideaUser = userService.selectUser(user.getUsername());
        if (null != ideaUser) {
            if (ideaUser.getUstate() == 0) {
                return -1;
            }
            if (repBody.length() > 0) {
                reply.setReplyMessage(repBody);
                Date date = new Date();
                reply.setIdeaUser(ideaUser);
                reply.setReplyTime(date);
                post.setLastReplyTime(date);
                reply.setPost(post);
                Integer addRepNum =  replyService.sendReply(reply);
                return addRepNum;
            }
        }
        return -2;
    }


    @RequestMapping(value = "/deleteReply")
    @ResponseBody
    public Integer deleteReply(@RequestParam("rid") Long rid,@RequestParam("repName") String repName,@AuthenticationPrincipal UserDetails user){
        IdeaUser ideaUser = userService.selectUser(user.getUsername());
        if(null != ideaUser && ideaUser.getLevel() >= 1 && user.getUsername().equals(repName)) {
            Integer delNum = replyService.deleteReplyByRid(rid);
            return delNum;
        }else {
            return 0;
        }
    }


}
