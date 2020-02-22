package com.seizedays.ideasharingprovider.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.seizedays.beans.Page;
import com.seizedays.beans.Post;
import com.seizedays.ideasharingprovider.mappers.PostMapper;
import com.seizedays.ideasharingprovider.mappers.ReplyMapper;
import com.seizedays.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostMapper postMapper;

    @Autowired
    private ReplyMapper replyMapper;

    @Override
    public Integer addPost(Post post) {
       int addNum = postMapper.addPost(post);
       return addNum;
    }

    @Override
    public List<Post> selectAllPost() {

        List<Post> postList = postMapper.selectAllPost();

        //查询帖子回复数
        for (Post post : postList){
            post.setReplyCount(replyMapper.getReplyCountByPid(post.getPid()));
        }

        return postList;
    }

    @Override
    public Post selectPostByPid(Long pid) {
        Post post = postMapper.selectPostById(pid);
        if(null != post) {
            post.setReplyCount(replyMapper.getReplyCountByPid(pid));
        }
        return post;
    }

    @Override
    public Post selectTempSavedPost(Long uid) {
        Post post = postMapper.selectTempSavedPost(uid);
        return post;
    }

    @Override
    public Integer updatePost(Post post) {
        return postMapper.updatePost(post);
    }

    @Override
    public Integer updatePostState(Integer stateNum, Long pid) {
        return postMapper.updatePostState(stateNum, pid);
    }


    @Override
    public Integer deletePost(Long pid) {
        //删除帖子下所有回复
        replyMapper.deleteReply(pid);
        //删除帖子
        int deleteNum = postMapper.deletePost(pid);
        return deleteNum;
    }

    @Override
    public List<Post> selectPostByUid(Long uid) {
        return postMapper.selectPostByUid(uid);
    }

    @Override
    public Page<Post> selectPostByPage(Map<String, Object> map) {
        Page<Post> page = new Page<>();
        map.put("showPage", page.getShowCount().longValue());
        page.setCurrentPage(((Integer) map.get("startPage") + 1));
        map.put("startPostCount", (Integer)map.get("startPage") * page.getShowCount());
        Integer opt = (Integer)map.get("opt");
        switch (opt){
            //根据opt的值执行响应的搜索
            case 0:
                System.out.println("case 0 执行");
                page.setModelList(postMapper.searchPostByTitle(map));
                break;
            case 1:
                System.out.println("case 1 执行");
                page.setModelList(postMapper.searchPostByAuthor(map));
                break;
            default:
                System.out.println("case default 执行");
                page.setModelList(postMapper.selectPostByPage(map));
                break;
        }

        Integer postCount = postMapper.getPostCount();


        page.setPageTotal(postCount % page.getShowCount() == 0 ? postCount / page.getShowCount() : (postCount / page.getShowCount()) + 1);
        for (Post post : page.getModelList()) {//查询回复数
            post.setReplyCount(replyMapper.getReplyCountByPid(post.getPid()));
        }
        return page;
    }

    @Override
    public Long findReplyCount(Long pid) {
        return replyMapper.getReplyCountByPid(pid);
    }

    @Override
    public Long findMaxPid() {
        return postMapper.findMaxPid();
    }
}
