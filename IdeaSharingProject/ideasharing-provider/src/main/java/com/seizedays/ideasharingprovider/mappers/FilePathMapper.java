package com.seizedays.ideasharingprovider.mappers;

import com.seizedays.beans.FilePath;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface FilePathMapper {
    Integer addPostFile(FilePath filePath);

    Integer addReplyFile(FilePath filePath);

    //删除Post下的全部文件
    Integer deletePostFile(Long pid);

    //删除Reply下的全部文件
    Integer deleteReplyFile(Long rid);

    //删除单个文件 <页面生成文件列表的时候 标签接收后台传的fid作为自己的id
    Integer deleteFileByFid(Long fid);

    FilePath selectFileByNameAndPid(Long pid,String fileName);
    FilePath selectFileByNameAndRid(Long rid,String fileName);
    FilePath selectFileByFid(Long fid);

    List<FilePath> selectPostFiles(Long pid);

    List<FilePath> selectReplyFiles(Long rid);
}