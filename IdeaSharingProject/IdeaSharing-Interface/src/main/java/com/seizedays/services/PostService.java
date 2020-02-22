package com.seizedays.services;

import com.seizedays.beans.Page;
import com.seizedays.beans.Post;

import java.util.List;
import java.util.Map;

public interface PostService {
    //新增帖子
    Integer addPost(Post post);

    //查询所有帖子
    List<Post> selectAllPost();

    //查询单一帖子
    Post selectPostByPid(Long pid);

    //查询暂存状态的帖子
    Post selectTempSavedPost(Long uid);

    //更新暂存的帖子内容
    Integer updatePost(Post post);

    //更新帖子的状态(暂存&已发布)
    Integer updatePostState(Integer stateNum, Long pid);

    //删除帖子
    Integer deletePost(Long pid);

    //根据uid查询帖子
    List<Post> selectPostByUid(Long uid);

    //分页查询帖子
    Page<Post> selectPostByPage(Map<String,Object> map);

    //获取单一帖子的回复数
    Long findReplyCount(Long pid);

    //获取最大pid
    Long findMaxPid();
}
