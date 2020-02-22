package com.seizedays.ideasharingprovider.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.seizedays.beans.IdeaUser;
import com.seizedays.ideasharingprovider.mappers.UserMapper;
import com.seizedays.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserMapper userMapper;

    @Override
    public Integer addUser(IdeaUser ideaUser) {
        return userMapper.addUser(ideaUser);
    }

    @Override
    public Integer deleteUser(String username) {
        return userMapper.deleteUser(username);
    }

    @Override
    public Integer updateUser(IdeaUser ideaUser) {
        return userMapper.updateUserPassword(ideaUser);
    }

    @Override
    public Integer updateUserData(IdeaUser ideaUser){
        return userMapper.updateUserData(ideaUser);
    }

    @Override
    public Integer updateUserAuth(IdeaUser ideaUser) {
        return userMapper.updateUserAuth(ideaUser);
    }


    @Override
    public IdeaUser selectUser(String username) {
        return userMapper.selectUser(username);
    }

    @Override
    public IdeaUser selectUserByUId(Long uid){
        return userMapper.selectUserByUId(uid);
    }

    @Override
    public List<IdeaUser> selectAllUser() {
        return userMapper.selectAllUser();
    }

    @Override
    public Integer duplicateCheck(String nickName) {
        return userMapper.duplicateCheck(nickName);
    }
}
