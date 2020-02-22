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
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class ViewController {

    @Reference
    PostService postService;

    @Reference
    ReplyService replyService;

    @Reference
    UserService userService;

    @RequestMapping("/register")
    public String toRegist(){
        return "register";
    }

    @RequestMapping("/login")
    public String toLogin(){
        return "login";
    }

    @RequestMapping("/index")
    public String mainPage(Model model){
        return "index";
    }





    @RequestMapping("/Calendar")
    public String toCalendar(){
        return "calendar";
    }


    @RequestMapping("/editor")
    public String editPost(Model model,@AuthenticationPrincipal UserDetails user){
        IdeaUser ideaUser = userService.selectUser(user.getUsername());
        Post post = postService.selectTempSavedPost(ideaUser.getUid());
        if(null != post) {
            model.addAttribute("tempPost", post);
        }else {
            post = new Post();
            post.setPid(-1L);
            model.addAttribute("tempPost", post);
        }

        return "editor";
    }

    @RequestMapping("/lookAround")
    public String lookAround(){
        return "bbs";
    }


    @RequestMapping("/user")
    public String userManage(Model model, @AuthenticationPrincipal UserDetails user){
        String username = user.getUsername();
        IdeaUser ideaUser = userService.selectUser(username);
        model.addAttribute("loginUser", ideaUser);
        return "user";
    }

    @RequestMapping("/safety")
    public String safeConfig(){
        return "safety";
    }

    @RequestMapping("/commu/myPost")
    public String toMyPost(@AuthenticationPrincipal UserDetails userDetails, Model model){

        Long pid = 0L;
        IdeaUser ideaUser = userService.selectUser(userDetails.getUsername());
        List<Post> postList = postService.selectPostByUid(ideaUser.getUid());

        if (postList.size() == 0) {
            model.addAttribute("postList", 0);
        }else {
            //为postList的每一个Post加入回复的数量
            for(int i = 0; i < postList.size(); i++){
                 pid = postList.get(i).getPid();
                 Long replyCount = postService.findReplyCount(pid);
                 postList.get(i).setReplyCount(replyCount);
            }
            model.addAttribute("postList", postList);
        }

        return "personal-post";
    }

    @RequestMapping("/commu/myReply")
    public String toMyReply(@AuthenticationPrincipal UserDetails userDetails, Model model){

        IdeaUser ideaUser = userService.selectUser(userDetails.getUsername());
        List<Reply> replyList = replyService.selectReplyByUid(ideaUser.getUid());
        if (replyList.size() == 0) {
            model.addAttribute("replyList", 0);
        }else {
            model.addAttribute("replyList", replyList);
        }

        return "personal-reply";

    }

    @RequestMapping("/authority/manage")
    public String authManage(Model model){
        List<IdeaUser> userList = userService.selectAllUser();
        model.addAttribute("userList", userList);
        return "auth-manage";
    }

}
