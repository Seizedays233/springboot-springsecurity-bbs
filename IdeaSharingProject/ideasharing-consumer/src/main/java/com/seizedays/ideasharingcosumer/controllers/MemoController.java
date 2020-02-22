package com.seizedays.ideasharingcosumer.controllers;

import com.alibaba.dubbo.config.annotation.Reference;
import com.seizedays.beans.IdeaUser;
import com.seizedays.beans.Memo;
import com.seizedays.services.MemoService;
import com.seizedays.services.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MemoController {

    @Reference
    private UserService userService;

    @Reference
    private MemoService memoService;

    @RequestMapping("/MyCalendar/selectMemo")
    @ResponseBody
    public List<Memo> selectDay(@RequestParam("date") String date,@AuthenticationPrincipal UserDetails user){

        IdeaUser ideaUser = userService.selectUser(user.getUsername());
        Long uid = ideaUser.getUid();
        List<Memo> memos = memoService.getMemo(date, uid);
        return memos;
    }

    @RequestMapping("/MyCalendar/addMemo")
    public Integer addMemo(@RequestParam("date") String date, @RequestParam("content") String content,@AuthenticationPrincipal UserDetails user){

        IdeaUser ideaUser = userService.selectUser(user.getUsername());
        Long uid = ideaUser.getUid();

        Memo memo = new Memo();

        memo.setDate(date);
        memo.setContent(content);
        memo.setUid(uid);

        Integer addNum = memoService.addMemo(memo);
        System.out.println("成功增加 " + addNum + " 项日程");
        return addNum;
    }

    @RequestMapping("/MyCalendar/deleteMemo")
    public int deleteMemo( @RequestParam("id") Long mid,@AuthenticationPrincipal UserDetails user){

        IdeaUser ideaUser = userService.selectUser(user.getUsername());
        Long uid = ideaUser.getUid();

        Integer deleteNum = memoService.deleteMemoById(mid,uid);
        System.out.println("成功删除了 " + deleteNum + " 项日程");

        return deleteNum;
    }

    @RequestMapping("/MyCalendar/updateMemo")
    public int updateMemo(@RequestParam("date") String date, @RequestParam("id") Long mid, @RequestParam("content") String content,@AuthenticationPrincipal UserDetails user){

        IdeaUser ideaUser = userService.selectUser(user.getUsername());
        Long uid = ideaUser.getUid();

        Memo memo = new Memo();
        memo.setMid(mid);
        memo.setDate(date);
        memo.setContent(content);
        memo.setUid(uid);

        Integer updateNum = memoService.updateMemo(memo);
        System.out.println(updateNum + "项日程更新成功");

        return updateNum;
    }

}
