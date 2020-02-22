package com.seizedays.ideasharingcosumer.controllers;

import com.alibaba.dubbo.config.annotation.Reference;
import com.seizedays.services.PostService;
import com.seizedays.services.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;

@Controller
public class BBSController {

    Map<String, Object> map;

    @Reference
    private PostService postService;

    @Reference
    private UserService userService;


    @RequestMapping(value = "/bbs", method = RequestMethod.GET)
    public String index( Model model, @RequestParam(value = "page", required = false) Integer page,@RequestParam(value = "opt", required = false) Integer opt, @RequestParam(value = "content", required = false) String content) {
        map = new HashMap<>();
        map.put("startPage", null == page ? 0 : page - 1);
        System.out.println(opt);
        //直接打开论坛网页时opt为空
        if (null == opt){

            map.put("opt", -1);
            map.put("content", "default");
        }else {
            //进行关键字搜索的时候opt不为空
            map.put("opt", opt);
            map.put("content", content);
        }
        model.addAttribute("page", postService.selectPostByPage(map));
//        model.addAttribute("ideaUserName", user.getUsername());

        return "bbs";
    }

}
