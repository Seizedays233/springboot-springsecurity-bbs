package com.seizedays.ideasharingcosumer.controllers;

import com.alibaba.dubbo.config.annotation.Reference;
import com.seizedays.beans.Diary;
import com.seizedays.beans.IdeaUser;
import com.seizedays.ideasharingcosumer.configs.FilePathConfig;
import com.seizedays.ideasharingcosumer.utils.FileUtil;
import com.seizedays.services.DiaryService;
import com.seizedays.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController()
public class DiaryController {

    @Reference
    private UserService userService;

    @Autowired
    private FilePathConfig filePathConfig;

    @Reference
    private DiaryService diaryService;

    @RequestMapping("/MyCalendar/imageUpload")
    public Map<String, Object> imageUpload(@RequestParam("imgFile") MultipartFile file, @RequestParam("date") String date, @RequestParam("content") String content, HttpServletRequest request,@AuthenticationPrincipal UserDetails user){

        IdeaUser ideaUser = userService.selectUser(user.getUsername());
        Long uid = ideaUser.getUid();
        Diary diary = new Diary();
        String result_msg="";//上传结果信息
        Map<String,Object> root=new HashMap();

        if (file.getSize() / 1000 > 10000){
            result_msg="图片大小不能超过10M";
        }
        else{
            //判断上传文件格式
            String fileType = file.getContentType();
            if ("image/jpg".equals(fileType) || "image/png".equals(fileType) || "image/jpeg".equals(fileType)) {

                //设置文件保存路径前缀
                String uploadPrefix = filePathConfig.getFilePath();

                //获取原始文件名
                String fileName = file.getOriginalFilename();
                //获取上传文件名后缀
                String suffixName = fileName.substring(fileName.lastIndexOf("."));

                //自定义文件名为用户的uid
                fileName = "cover/" + uid + "/" + date + suffixName;

                String realPath =  uploadPrefix + "/attached/" + fileName;
                if (FileUtil.upload(file, realPath)) {
                    //文件存放的相对路径(一般存放在数据库用于img标签的src)
                    String relativePath="/attached/"+fileName;

                    root.put("relativePath",relativePath);//前端根据是否存在该字段来判断上传是否成功
                    result_msg="图片上传成功";
                    diary.setImageName(relativePath);
                    diary.setDate(date);
                    diary.setContent(content);
                    diary.setUid(uid);
                    diaryService.saveImage(diary);
                }
                else{
                    result_msg="图片上传失败";
                }
            }
            else{
                result_msg="图片格式不正确";
            }
        }
        root.put("result_msg",result_msg);

        return root;
    }

    @RequestMapping(value = "/MyCalendar/diaryOnLoad")
    public Diary diaryOnLoad(@RequestParam("date") String date,@AuthenticationPrincipal UserDetails user){

        IdeaUser ideaUser = userService.selectUser(user.getUsername());
        Long uid = ideaUser.getUid();

        Diary selectedDiary = diaryService.selectDiary(date,uid);
        if(selectedDiary != null) {
            return selectedDiary;
        }else {
            Diary fakeDiary = new Diary();
            fakeDiary.setContent("今天还没有写日记");
            return fakeDiary;
        }
    }

    @RequestMapping(value = "/MyCalendar/imgOnLoad")
    public Diary imageOnLoad(@RequestParam("date") String date,@AuthenticationPrincipal UserDetails user){

        IdeaUser ideaUser = userService.selectUser(user.getUsername());
        Long uid = ideaUser.getUid();

        Diary selectedDiary = diaryService.selectDiary(date, uid);
        if(selectedDiary != null) {
           return selectedDiary;
        }else {
            Diary fakeDiary = new Diary();
            fakeDiary.setImageName("/img/white.jpg");
            return fakeDiary;
        }
    }

    @RequestMapping("/MyCalendar/upLoadDiary")
    public Integer diaryUpLoad(@RequestParam("date") String date, @RequestParam("content") String content,@AuthenticationPrincipal UserDetails user){

        IdeaUser ideaUser = userService.selectUser(user.getUsername());
        Long uid = ideaUser.getUid();

        Diary selectedDiary = diaryService.selectDiary(date,uid);
        int saveNum = 0;
        if(selectedDiary != null) {
            selectedDiary.setContent(content);
            selectedDiary.setUid(uid);
            saveNum = diaryService.updateDiary(selectedDiary);
        }else {
            selectedDiary = new Diary();
            selectedDiary.setDate(date);
            selectedDiary.setContent(content);
            selectedDiary.setUid(uid);
            saveNum = diaryService.addDiary(selectedDiary);
        }
        return saveNum;
    }

    @RequestMapping("/MyCalendar/deleteDiary")
    public Integer deleteDiary(@RequestParam("date") String date,@AuthenticationPrincipal UserDetails user){

        IdeaUser ideaUser = userService.selectUser(user.getUsername());
        Long uid = ideaUser.getUid();

        Diary selectedDiary = diaryService.selectDiary(date, uid);

        int deleteNum = 0;
        if(selectedDiary != null) {
            deleteNum = diaryService.deleteDiary(date, uid);

        }
        return deleteNum;
    }


}
