package com.seizedays.ideasharingprovider.mappers;


import com.seizedays.beans.Reply;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
@Component
public interface ReplyMapper {

    //
    long getReplyCountByPid(Long pid);


    //根据pid查询回复
    List<Reply> selectReplyByPid(Long pid);

    //根据rid查询回复
    Reply selectReplyByRid(Long rid);

    //根据uid查询回复
    List<Reply> selectReplyByUid(Long uid);


    //发表回复
    Integer sendReply(Reply reply);


    //删除帖子下所有回复
    Integer deleteReply(Long pid);


    //分页查询回复
    List<Reply> selectReplyByPidAndPage(Map<String, Long> map);


    //根据id删除回复
    Integer deleteReplyByRid(Long rid);

}
