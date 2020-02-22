# springboot-springsecurity-bbs
模拟论坛网站的实现

# 基本功能
* 新用户注册、用户登录验证
*  个人信息、头像、密码的编辑修改
*  关键字查找
*  发帖、回帖、
*  上传/下载文件及插入图片功能
*  用户权限管理
*  私人日记 日程备忘录

# 项目视频演示
  视频地址：https://www.bilibili.com/video/av90445405#reply2410652124
  
# 项目使用技术/框架
* SpringBoot
* SpringSecurity
* Dubbo
* Thymeleaf
*	BootStrap
* jQuery
* KindEditor
* MySQL
* Tomcat

# How to Use
 1. 在Provider这个Moudle的application.properties文件中配置好 mysql数据源信息、 dubbo以及tomcat服务端口
 2. 在Consumer这个Moudle的application.properties文件中配置好  dubbo 和 tomcat服务端口
 3. 确保数据库正确导入idea_sharing.sql文件
 4. 启动Zookeeper
 5. 分别启动Provider和Consumer 即可正常运行
 
 # 联系方式
 * qq: 2945734401
 * email: 2945734401@qq.com


