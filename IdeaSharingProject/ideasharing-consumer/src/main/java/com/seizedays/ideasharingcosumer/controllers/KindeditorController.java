package com.seizedays.ideasharingcosumer.controllers;


import com.alibaba.dubbo.config.annotation.Reference;
import com.seizedays.beans.FilePath;
import com.seizedays.beans.IdeaUser;
import com.seizedays.beans.Post;
import com.seizedays.ideasharingcosumer.configs.FilePathConfig;
import com.seizedays.services.FilePathService;
import com.seizedays.services.PostService;
import com.seizedays.services.UserService;
import org.apache.tomcat.util.http.fileupload.FileItemFactory;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class KindeditorController {

    @Autowired
    private FilePathConfig filePathConfig;

    private String uploadPrefix;

    @Reference
    UserService userService;

    @Reference
    PostService postService;

    @Reference
    FilePathService filePathService;

    private String getError(String message) {
        JSONObject obj = new JSONObject();
        obj.put("error", 1);
        obj.put("message", message);
        return obj.toJSONString();
    }


    @RequestMapping("/uploadJson/{pram}")
    public void uploadJson(HttpServletRequest request, HttpServletResponse response, @AuthenticationPrincipal UserDetails user, @PathVariable String pram) throws Exception {
        IdeaUser loginUser = userService.selectUser(user.getUsername());
        System.out.println("pid " + pram);
        //获取参数
        String [] str = pram.split("\\.");
        Long pid = Long.valueOf(str[1]);

        //获取文件路径前缀
        uploadPrefix = filePathConfig.getFilePath();
        response.setContentType("application/json; charset=UTF-8");
        PrintWriter out = response.getWriter();
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        //文件保存目录路径
        String savePath = uploadPrefix + "/attached/";

        //定义允许上传的文件扩展名
        HashMap<String, String> extMap = new HashMap<String, String>();
        extMap.put("image", "gif,jpg,jpeg,png,bmp");
        extMap.put("flash", "swf,flv");
        extMap.put("media", "swf,flv,mp3,wav,wma,wmv,mid,avi,mpg,asf,rm,rmvb,mp4");
        extMap.put("file", "doc,docx,xls,xlsx,ppt,htm,html,txt,zip,rar,gz,bz2,pdf");

        //最大文件大小
        long maxSize = 1000000L;

        response.setContentType("text/html; charset=UTF-8");

        if (!ServletFileUpload.isMultipartContent(request)) {
            out.println(getError("请选择文件。"));
            return;
        }

        String dirName = request.getParameter("dir");
        if (dirName == null) {
            dirName = "image";
        }
        if (!extMap.containsKey(dirName)) {
            out.println(getError("文件类型不正确。"));
            return;
        }
        //pid小于0 说明页面为新帖 当且仅当文件类型为"file"时触发“占座”行为
        if(pid < 0 && "post".equals(str[0]) && "file".equals(dirName)){
            //手动设置pid 使保存路径的pid和数据库的pid一致
            pid = postService.findMaxPid() + 1L;
            Post post = new Post();
            post.setPid(pid);
            post.setIdeaUser(loginUser);
            post.setTempSave(1);

            //在数据库“占座”
            postService.addPost(post);
        }
        //创建文件夹
        if("post".equals(str[0])) {
            savePath += dirName + "/" + user.getUsername() + "/" + str[0] + "/" + pid + "/";
        }else {
            savePath += dirName + "/" + user.getUsername() + "/" + str[0] + "/" + str[1] + "/";
        }
//        saveUrl += dirName + "/" + user.getUsername() + "/" + str[0] + "/" + str[1] + "/";


        File saveDirFile = new File(savePath);
        if (!saveDirFile.exists()) {
            saveDirFile.mkdirs();
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String ymd = sdf.format(new Date());
        File dirFile = new File(savePath);
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }

        FileItemFactory factory = new DiskFileItemFactory();
        ServletFileUpload upload = new ServletFileUpload(factory);
        upload.setHeaderEncoding("UTF-8");
        //此处是直接采用Spring的上传
        for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
            MultipartFile mf = entity.getValue();
            String fileFullname = mf.getOriginalFilename();
            fileFullname = fileFullname.replace('&', 'a');
            fileFullname = fileFullname.replace(',', 'b');
            fileFullname = fileFullname.replace('，', 'c');
            //检查扩展名
            String fileExt = fileFullname.substring(fileFullname.lastIndexOf(".") + 1).toLowerCase();
            if (!Arrays.<String>asList(extMap.get(dirName).split(",")).contains(fileExt)) {
                out.println(getError("上传文件扩展名是不允许的扩展名。\n只允许" + extMap.get(dirName) + "格式。"));
                return;
            }

            SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
            String newFileName = df.format(new Date()) + "_" + new Random().nextInt(1000) + "." + fileExt;

            File uploadFile = null;
            String totalPath = "";

            try {
                JSONObject obj = new JSONObject();
                if (extMap.get("file").contains(fileExt)) {

//                    if("post".equals(str[0]))
                    FilePath exPath = filePathService.selectFileByNameAndPid(pid,fileFullname);
                    totalPath ="/attached/" + dirName + "/" + user.getUsername() + "/" + str[0] + "/" + pid + "/"  + fileFullname;
                    if(null == exPath){
                        uploadFile = new File(savePath + fileFullname);
                        FileCopyUtils.copy(mf.getBytes(), uploadFile);
                        exPath = new FilePath();
                        exPath.setPid(pid);
                        exPath.setFilePath(totalPath);
                        exPath.setFileName(fileFullname);
                        filePathService.addPostFile(exPath);
                    }else {
                        out.println(getError("文件名已存在，请重试。"));
                    }
                } else {
                    uploadFile = new File(savePath + newFileName);
                    FileCopyUtils.copy(mf.getBytes(), uploadFile);
                    totalPath = request.getContextPath() + "/attached/" + dirName + "/" + user.getUsername() + "/" + str[0] + "/" + str[1] + "/"  + newFileName;
                    System.out.println("contextPath" + request.getContextPath());
                    System.out.println("totalPath" + totalPath);
                    obj.put("url", totalPath);
                }

                //文件访问完整路径
//                String totalPath = request.getContextPath() + "/attached/" + dirName + "/" + user.getUsername() + "/" + str[0] + "/" + str[1] + "/"  + fileFullname;
                obj.put("error", 0);
                //根据文件上传位置保存文件路径到数据库中



                out.println(obj.toJSONString());
            } catch (IOException e) {
                e.printStackTrace();
                out.println(getError("上传文件失败。"));
                return;
            }
        }
    }

}
