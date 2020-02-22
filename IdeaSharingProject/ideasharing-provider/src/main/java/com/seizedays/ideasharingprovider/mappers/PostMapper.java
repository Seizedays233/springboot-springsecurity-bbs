package com.seizedays.ideasharingprovider.mappers;

import com.seizedays.beans.Post;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
@Component
public interface PostMapper {


    //发表帖子
    int addPost(Post post);


    //查询所有帖子
    List<Post> selectAllPost();

    //根据id查询帖子
    Post selectPostById(Long pid);

    //根据uid查询帖子
    List<Post> selectPostByUid(Long uid);

    //更新暂存的帖子
    Integer updatePost(Post post);

    //更新回复时间
    Integer updatePostLastReplyTime(Post post);

    //更新帖子的状态
    Integer updatePostState(Integer stateNum, Long pid);

    //查询暂存状态的帖子
    Post selectTempSavedPost(Long uid);

    //删除帖子
    Integer deletePost(Long pid);


    //分页显示全部帖子
    List<Post> selectPostByPage(Map<String, Object> map);

    //根据标题查询帖子（模糊查询）
    List<Post> searchPostByTitle(Map<String, Object> map);

    //根据作者查询帖子（模糊查询）
    List<Post> searchPostByAuthor(Map<String, Object> map);

    //查询帖子数量
    Integer getPostCount();

    //获取最大pid
    Long findMaxPid();
}
