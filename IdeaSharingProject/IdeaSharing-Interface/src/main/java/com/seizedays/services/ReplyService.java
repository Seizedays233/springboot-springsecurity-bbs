package com.seizedays.services;

import com.seizedays.beans.Page;
import com.seizedays.beans.Reply;

import java.util.List;
import java.util.Map;

public interface ReplyService {

    //根据pid查询回复帖
    List<Reply> selectReplyByPid(Long pid);

    //根据rid查询回复帖
    Reply selectReplyByRid(Long rid);

    //根据uid查询回复
    List<Reply> selectReplyByUid(Long uid);

    //发表回复
    Integer sendReply(Reply reply);

    //分页查询回复
    Page<Reply> selectReplyByPidAndPage(Map<String,Long> map);


    //删除回复
    Integer deleteReplyByRid(Long rid);
}
