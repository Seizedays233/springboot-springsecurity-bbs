package com.seizedays.ideasharingcosumer.controllers;

import com.alibaba.dubbo.config.annotation.Reference;
import com.seizedays.beans.FilePath;
import com.seizedays.beans.IdeaUser;
import com.seizedays.beans.Post;
import com.seizedays.ideasharingcosumer.configs.FilePathConfig;
import com.seizedays.ideasharingcosumer.utils.FileUtil;
import com.seizedays.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;


@Controller
public class PostController {

    @Reference
    private UserService userService;

    @Reference
    private PostService postService;

    @Reference
    private ReplyService replyService;

    @Reference
    private FilePathService filePathService;

    @Autowired
    private FilePathConfig filePathConfig;


    @RequestMapping(value = "/savePost.do", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> savePost(@AuthenticationPrincipal UserDetails user, @RequestParam("ptitle") String ptitle, @RequestParam("pbody") String pbody,@RequestParam("pid") Long lpid) {
        HashMap<String, Object> message = new HashMap<>();
        Post post = new Post();
        IdeaUser ideaUser = userService.selectUser(user.getUsername());
//        Long lpid = Long.valueOf(pid);
        System.out.println("savePid " + lpid);

        if (null != ideaUser) {
            if (ideaUser.getUstate() == 0) {
                message.put("msg","用户已被禁言，不能保存");
                message.put("pid",-1L);
                return message;
            }
            if(lpid > 0) {
                //当lpid>0时 说明页面显示的是暂存内容
                post = postService.selectPostByPid(lpid);
            }else {
                //当lpid<0时 可能会通过上传文件的形式新增了暂存状态的post 详见KindeditorController
                post = postService.selectTempSavedPost(ideaUser.getUid());
            }

            if ((ptitle.length() > 0 && ptitle.length() <= 80) && (pbody.length() > 0)){
                //输入的标题和内容长度符合要求时  执行下面的代码
                if(null == post) {
                    post = new Post();

                    //lpid小于0时 数据库会自动分配Pid
                    if(lpid > 0) {
                        post.setPid(lpid);
                    }
                    post.setPtitle(ptitle);
                    post.setPbody(pbody);
                    post.setIdeaUser(ideaUser);
//                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = new Date();
                    post.setPsendTime(date);
                    post.setLastReplyTime(date);
                    post.setTempSave(1);

                    Integer addNum = postService.addPost(post);
                    System.out.println("成功保存 " + addNum + "条帖子");
                    message.put("msg","保存成功");
                    message.put("pid",lpid);
                    return message;
                }else {
                    //post不为空的时候
                    post.setPtitle(ptitle);
                    post.setPbody(pbody);
//                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = new Date();
                    post.setPsendTime(date);
                    post.setLastReplyTime(date);
                    post.setTempSave(1);
                    Integer updateNum = postService.updatePost(post);

                    message.put("msg","保存成功");
                    message.put("pid",post.getPid());
                    return message;
                }
            }
        }
        message.put("msg","用户不存在");
        message.put("pid",-1L);
        return message;
    }

    @RequestMapping(value = "/sendPost.do")
    @ResponseBody
    public Integer sendPost(@RequestParam("pid") Long lpid){
        Integer updateNum = postService.updatePostState(0, lpid);
        return updateNum;
    }


    @RequestMapping(value = {"/post/{pid}.content"}, method = RequestMethod.GET)
    public String replyPage(@PathVariable Long pid, @RequestParam(value = "page", required = false) Long page,
                            Model model,@AuthenticationPrincipal UserDetails user, @RequestParam(value = "rid", required = false) Long rid) {

        String username = user.getUsername();

        //根据pid查询帖子
        model.addAttribute("post", postService.selectPostByPid(pid));

        //根据pid分页查询回复
        Map<String, Long> map = new HashMap<>();
        map.put("pid", pid);
        map.put("startPage", page == null ? 0L : page - 1);
        model.addAttribute("page", replyService.selectReplyByPidAndPage(map));
        model.addAttribute("actUsername",username);

        //定位回复位置（如需）
        if (null == rid){
            model.addAttribute("rid",-1);
        }else {
            model.addAttribute("rid", rid);
        }

        return "post";
    }



    @RequestMapping(value = "/deletePost")
    @ResponseBody
    public Integer deletePost(@RequestParam("pid") Long pid, @AuthenticationPrincipal UserDetails user) {

        IdeaUser ideaUser = userService.selectUser(user.getUsername());
        Post post = postService.selectPostByPid(pid);

        if ((null != ideaUser && ideaUser.getLevel() == 1) && ideaUser.getUsername().equals(post.getIdeaUser().getUsername())) {
            String totalDir = "";

            String[] dirNames = {"image", "file", "media", "flash"};

            for (String dirName : dirNames){
                totalDir = filePathConfig.getFilePath() + "/attached/" + dirName + "/" + user.getUsername() + "/post/" + pid;
                File file = new File(totalDir);
                if(file.exists()) {
                    FileUtil.deleteFile(file);
                }
            }

            return postService.deletePost(pid);
        }

        return 0;
    }

    @RequestMapping("/postFileOnLoad")
    @ResponseBody
    public List<FilePath> postFileOnload(HttpServletResponse response, @RequestParam("pid") Long pid){
        response.setContentType("application/json; charset=UTF-8");
        //查询pid下的全部文件路径
        return filePathService.selectPostFiles(pid);
    }

    @RequestMapping("/downloadFile/{fid}")
    public void download(HttpServletResponse response, @PathVariable Long fid) throws IOException {
        response.setContentType("text/json; charset=utf-8");
        FilePath filePath = filePathService.selectFileByFid(fid);
        if( null == filePath){
            return;
        }
        String fileName = filePathConfig.getFilePath() + filePath.getFilePath();
        File file = new File(fileName);
        if(file.exists()) {
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(new File(fileName)));
            byte[] buff = new byte[1024];
            ServletOutputStream outputStream = response.getOutputStream();
            fileName = fileName.replace("\\", "_");
            System.out.println(fileName.substring(fileName.lastIndexOf("_") + 1));
            //设置响应头
            response.setHeader("content-disposition", "attachment;filename=" + fileName.substring(fileName.lastIndexOf("_") + 1));
            BufferedOutputStream bos = new BufferedOutputStream(outputStream);
            int count = 0;
            while ((count = bis.read(buff)) != -1) {
                bos.write(buff, 0, count);
            }
            bos.flush();
            bos.close();
            bis.close();
        }
    }

    @RequestMapping("/removeFile")
    @ResponseBody
    public String removeFile(HttpServletResponse response, @RequestParam("fid") Long fid,@AuthenticationPrincipal UserDetails user){
        response.setContentType("text/json; charset=utf-8");
        FilePath filePath = filePathService.selectFileByFid(fid);
        if(null == filePath){
            return "文件路径不存在";
        }
        Long pid = filePath.getPid();

        Post post = postService.selectPostByPid(pid);
        //判断执行删除的内容是否是当前登录用户上传 若用户名不符 拒绝删除
        if(!post.getIdeaUser().getUsername().equals(user.getUsername())){
            return "用户权限不符";
        }
        String fileName = filePathConfig.getFilePath() + filePath.getFilePath();
        System.out.println(fileName);
        File delFile = new File(fileName);
        if(delFile.exists()){
            if(delFile.delete()){
                filePathService.deleteFileByFid(fid);
                return "删除成功";
            }else {
                return "删除失败";
            }
        }else {
            return "文件不存在";
        }
    }

}
