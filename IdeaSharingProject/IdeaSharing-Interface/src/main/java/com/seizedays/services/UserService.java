package com.seizedays.services;

import com.seizedays.beans.IdeaUser;

import java.util.List;

public interface UserService {
    //注册新用户
    Integer addUser(IdeaUser ideaUser);
    //删除用户
    Integer deleteUser(String username);
    //更新用户信息
    Integer updateUser(IdeaUser ideaUser);
    //查询单个用户
    IdeaUser selectUser(String username);
    //根据uid查询用户
    IdeaUser selectUserByUId(Long uid);

    //查询所有用户
    List<IdeaUser> selectAllUser();

    //用户昵称查重
    Integer duplicateCheck(String nickName);

    //更新用户资料
    Integer updateUserData(IdeaUser ideaUser);

    //更新用户权限
    Integer updateUserAuth(IdeaUser ideaUser);


}
