package com.seizedays.ideasharingprovider.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.seizedays.beans.Page;
import com.seizedays.beans.Reply;
import com.seizedays.ideasharingprovider.mappers.PostMapper;
import com.seizedays.ideasharingprovider.mappers.ReplyMapper;
import com.seizedays.services.ReplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@Service
public class ReplyServiceImpl implements ReplyService {

    @Autowired
    private ReplyMapper replyMapper;

    @Autowired
    private PostMapper postMapper;


    @Override
    public List<Reply> selectReplyByPid(Long pid) {
        return replyMapper.selectReplyByPid(pid);
    }

    @Override
    public Reply selectReplyByRid(Long rid) {
        return replyMapper.selectReplyByRid(rid);
    }

    @Override
    public List<Reply> selectReplyByUid(Long uid) {
        return replyMapper.selectReplyByUid(uid);
    }

    @Override
    public Integer sendReply(Reply reply) {
        postMapper.updatePostLastReplyTime(reply.getPost());
        return replyMapper.sendReply(reply);
    }

    @Override
    public Page<Reply> selectReplyByPidAndPage(Map<String, Long> map) {
        Page<Reply> page = new Page<>();
        //每页显示10条回复
        page.setShowCount(10);
        map.put("showPage", page.getShowCount().longValue());
        //设置当前页 从controller获取startPage参数
        page.setCurrentPage((int)(map.get("startPage") +1));

        //当前页面的第一条回复的序号
        map.put("startReplyCount",map.get("startPage")*page.getShowCount());

        //将查询结果存入page
        page.setModelList(replyMapper.selectReplyByPidAndPage(map));

        //总回复数
        Integer postCount = (int)replyMapper.getReplyCountByPid(map.get("pid").longValue());

        page.setPageTotal(postCount % page.getShowCount() == 0 ? postCount / page.getShowCount() : (postCount / page.getShowCount()) + 1);//计算分页页数
        return page;
    }

    @Override
    public Integer deleteReplyByRid(Long rid) {
        return replyMapper.deleteReplyByRid(rid);
    }
}
