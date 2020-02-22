package com.seizedays.services;

import com.seizedays.beans.FilePath;

import java.util.List;

public interface FilePathService {
    Integer addPostFile(FilePath filePath);
    Integer addReplyFile(FilePath filePath);
    Integer deletePostFile(Long pid);
    Integer deleteRepleFile(Long rid);
    Integer deleteFileByFid(Long fid);
    FilePath selectFileByNameAndPid(Long pid,String fileName);
    FilePath selectFileByNameAndRid(Long rid,String fileName);
    FilePath selectFileByFid(Long fid);
    List<FilePath> selectPostFiles(Long pid);
    List<FilePath> selectReplyFiles(Long rid);
}
