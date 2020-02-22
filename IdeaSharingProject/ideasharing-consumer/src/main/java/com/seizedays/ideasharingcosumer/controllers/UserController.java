package com.seizedays.ideasharingcosumer.controllers;

import com.alibaba.dubbo.config.annotation.Reference;
import com.seizedays.beans.IdeaUser;
import com.seizedays.ideasharingcosumer.configs.FilePathConfig;
import com.seizedays.ideasharingcosumer.utils.FileUtil;
import com.seizedays.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.regex.Pattern;

@Controller
public class UserController {

    @Autowired
    private FilePathConfig filePathConfig;

    @Reference
    private UserService userService;



    @RequestMapping("/register.do")
    public String registUser(Model model,
                             @RequestParam("username") String username,
                             @RequestParam("email") String email,
                             @RequestParam("phone") String phone,
                             @RequestParam("password") String password,
                             @RequestParam("repassword") String repeatPwd) {

        IdeaUser ideaUser = new IdeaUser();

        //输出验证信息
        String msg = "";

        //邮箱正则表达式
        String mailPattern = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";

        //电话的正则表达式
        String phonePattern = "^1[0-9]{10}$";

        //验证用户名输入格式
        if (username == null || username.length() == 0) {

            msg = "*用户名不能为空！";
            model.addAttribute("userMsg", msg);
            System.out.println(msg);
            return "register";

        } else if (username.length() < 3 || username.length() > 15) {

            msg = "*用户名长度不符合规范（3-15个字符）";
            System.out.println(msg);
            model.addAttribute("userMsg", msg);
            return "register";

        } else {

            ideaUser = userService.selectUser(username);

            if (ideaUser != null) {

                msg = "*用户名已被注册";
                System.out.println(msg);
                model.addAttribute("userMsg", msg);
                return "register";

            }

        }

        //验证邮箱格式
        if(!Pattern.matches(mailPattern, email)){
            msg = "*邮箱格式不正确";
            model.addAttribute("emailMsg", msg);
            System.out.println(msg);
            return "register";
        }

        //验证电话格式
        if(!Pattern.matches(phonePattern,phone)){
            msg = "*电话号码格式不正确";
            model.addAttribute("phoneMsg", msg);
            System.out.println(msg);
            return "register";
        }

        //验证密码输入格式
        if(password == null || password.length() == 0){

            msg = "*密码不能为空！";
            model.addAttribute("pwdMsg", msg);
            System.out.println(msg);
            return "register";

        }else if(password.length()<6 || password.length()>16){

            msg = "*密码长度不符合规范(6-16个字符)";
            model.addAttribute("pwdMsg", msg);
            System.out.println(msg);
            return "register";

        }else if(!password.equals(repeatPwd)){

            msg = "*两次密码输入不一致";
            model.addAttribute("pwdMsg", msg);
            System.out.println(msg);
            return "register";

        }
        String encryPassword = new BCryptPasswordEncoder().encode(password);
        ideaUser = new IdeaUser();
        ideaUser.setUsername(username);
        ideaUser.setPassword(encryPassword);
        ideaUser.setEmail(email);
        ideaUser.setPhone(phone);
        ideaUser.setLevel(1);
        ideaUser.setUstate(1);
        ideaUser.setUcreatetime(new Date());
        //为用户随机生成一个默认昵称
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmm");
        String defaultNickName = df.format(new Date()) + new Random().nextInt(1000) ;
        ideaUser.setNickName(defaultNickName);
        ideaUser.setProfilePath("/images/default.jpg");
        ideaUser.setAuthority("USER");
        ideaUser.setRole("ROLE_VIP1");


        int addNum = userService.addUser(ideaUser);

        //赋予权限

        System.out.println("成功新增: " + addNum + " 名用户");

        return "registSuccess";
    }

    @RequestMapping("/login.fail")
    public String loginFail(Model model){
        String loginMsg = "*用户名不存在或密码不正确";
        model.addAttribute("loginMessage", loginMsg);
        return "login";
    }

    @RequestMapping("/duplicateCheck")
    @ResponseBody
    public Integer dublicateCheck(@RequestParam("username") String username){

        IdeaUser user = userService.selectUser(username);

        if(user != null){
            return 1;
        }else {
            return 0;
        }
    }

    @RequestMapping("/duplicateNick")
    @ResponseBody
    public Integer dublicateNick(@AuthenticationPrincipal UserDetails user, @RequestParam("nickName") String nickName){

        IdeaUser ideaUser = userService.selectUser(user.getUsername());
        if(!ideaUser.getNickName().equals(nickName)) {
            Integer duplicateNum = userService.duplicateCheck(nickName);
            System.out.println("重复个数" + duplicateNum);
            return duplicateNum;
        }

        return 0;
    }

    @RequestMapping(value = "/updateUser")
    @ResponseBody
    public Map<String, String> updateUser(@AuthenticationPrincipal UserDetails user, @RequestParam(value= "nickname", required = false) String nickName, @RequestParam(value = "email",required = false) String email, @RequestParam(value = "phone",required = false) String phone,
                                          @RequestParam(value = "signature",required = false) String signature, @RequestParam(value = "sex",required = false) boolean sex){
        //输出验证信息
        String msg = "";

        Map<String, String> reMsg = new HashMap<>();

        //邮箱正则表达式
        String mailPattern = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";

        //电话的正则表达式
        String phonePattern = "^1[0-9]{10}$";

        //验证邮箱格式
        if(!Pattern.matches(mailPattern, email)){
            msg = "*邮箱格式不正确";
            reMsg.put("tips","mailTip");
            reMsg.put("mailTip", msg);
            return  reMsg;
        }

        //验证电话格式
        if(!Pattern.matches(phonePattern,phone)){
            msg = "*电话号码格式不正确";
            reMsg.put("tips", "phoneTip");
            reMsg.put("phoneTip", msg);
            return  reMsg;
        }

        //验证昵称输入格式
        if (nickName == null || nickName.length() == 0) {
            msg = "*昵称不能为空！";
            reMsg.put("tips", "nickNameTip");
            reMsg.put("nickNameTip", msg);
            return  reMsg;

        } else if (nickName.length() < 3 || nickName.length() > 15) {

            msg = "*昵称长度不符合规范（3-15个字符）";
            reMsg.put("tips", "nickNameTip");
            reMsg.put("nickNameTip", msg);
            return  reMsg;
        }

        IdeaUser ideaUser = userService.selectUser(user.getUsername());

        if(!ideaUser.getNickName().equals(nickName)) {
            Integer duplicateNum = userService.duplicateCheck(nickName);
            if (duplicateNum > 0){
                msg = "*昵称已被占用";
                reMsg.put("tips", "nickNameTip");
                reMsg.put("nickNameTip", msg);
                return  reMsg;
            }
        }

        ideaUser.setNickName(nickName);
        ideaUser.setEmail(email);
        ideaUser.setPhone(phone);
        ideaUser.setSignature(signature);
        ideaUser.setSex(sex);
        int updateNum =  userService.updateUserData(ideaUser);
        if(updateNum > 0) {
            msg = "保存成功";
        }else {
            msg = "保存失败";
        }
        reMsg.put("saveStatus", msg);
        return  reMsg;
    }

    @RequestMapping("/updatePwd")
    public String updatePwd(@AuthenticationPrincipal UserDetails user,@RequestParam("oriPwd") String oriPwd, @RequestParam("newPwd") String newPwd,
                            @RequestParam("confirmPwd") String confirmPwd, @RequestParam("email") String email, @RequestParam("phone") String phone,
                            Model model){

        IdeaUser ideaUser = userService.selectUser(user.getUsername());


        //刷新页面前保存用户输入的email和phone数据
        model.addAttribute("email", email);
        model.addAttribute("phone", phone);

        //对加密的密码校验   方法中前一个参数为前端传来的值（例如123），后一个为数据库中需要对比的值（已加密存入数据库的密码)
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (encoder.matches(oriPwd, ideaUser.getPassword())){
            if(ideaUser.getEmail().equals(email) || ideaUser.getPhone().equals(phone)){
                if(!confirmPwd.equals(newPwd)){
                    model.addAttribute("newPwdTip", "*两次输入的新密码不一致，请重试");
                    return "safety";
                }

                //对新密码进行编码加密
                newPwd = encoder.encode(newPwd);
                ideaUser.setPassword(newPwd);
                userService.updateUser(ideaUser);
                return "changeSuccess";


            }else {
                model.addAttribute("emailTips", "*邮箱或电话号码不正确，请重试");
                return "safety";

            }
        }else{
            model.addAttribute("pwdTips","*原密码不正确，请重试");
            return "safety";
        }

    }

    @RequestMapping("/profileUpload")
    @ResponseBody
    public Map<String, String> prifileUpload(@AuthenticationPrincipal UserDetails user,@RequestParam("imgFile") MultipartFile file, HttpServletRequest request){
        IdeaUser ideaUser = userService.selectUser(user.getUsername());
        Long uid = ideaUser.getUid();
        String result_msg="";//上传结果信息
        Map<String, String> root=new HashMap();

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
                fileName = "profile/" + uid + suffixName;

                String realPath =  uploadPrefix + "/attached/" + fileName;
                if (FileUtil.upload(file, realPath)) {
                    //文件存放的相对路径(一般存放在数据库用于img标签的src)
                    String relativePath="/attached/"+fileName;
                    ideaUser.setProfilePath(relativePath);
                    userService.updateUserData(ideaUser);
                    root.put("relativePath",relativePath);
                    result_msg="图片上传成功";

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

    //更新用户名及头像
    @RequestMapping("/auth")
    @ResponseBody
    public Map<String, String> auth(@AuthenticationPrincipal UserDetails user){
        Map<String, String> msg = new HashMap<>();
        String username = user.getUsername();
        IdeaUser user1 = userService.selectUser(username);
        msg.put("nickName", user1.getNickName());
        msg.put("profilePath", user1.getProfilePath());
        return msg;
    }

    //通过SpringSercurity过滤 只有ADMIN用户能够访问这个方法
    @RequestMapping("/auth/update")
    @ResponseBody
    public Integer updateAuth(@RequestParam("uid") Long uid, @RequestParam("role") String role, @RequestParam("authority") String authority,
                              @RequestParam("ustate") Integer ustate){
        IdeaUser ideaUser = userService.selectUserByUId(uid);
        ideaUser.setRole(role);
        ideaUser.setAuthority(authority);
        ideaUser.setUstate(ustate);

        Integer updateNum = userService.updateUserAuth(ideaUser);

        return updateNum;
    }


}
