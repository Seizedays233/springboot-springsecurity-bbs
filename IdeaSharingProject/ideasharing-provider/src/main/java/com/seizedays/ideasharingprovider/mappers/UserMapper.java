package com.seizedays.ideasharingprovider.mappers;

import com.seizedays.beans.IdeaUser;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface UserMapper {
    Integer addUser(IdeaUser ideaUser);
    IdeaUser selectUser(String username);
    IdeaUser selectUserByUId(Long uid);
    Integer deleteUser(String username);
    Integer updateUserPassword(IdeaUser ideaUser);
    Integer duplicateCheck(String nickName);
    Integer updateUserData(IdeaUser ideaUser);
    List<IdeaUser> selectAllUser();
    Integer updateUserAuth(IdeaUser ideaUser);

}
