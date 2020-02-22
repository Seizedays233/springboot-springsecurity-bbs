package com.seizedays.ideasharingprovider.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.seizedays.beans.FilePath;
import com.seizedays.ideasharingprovider.mappers.FilePathMapper;
import com.seizedays.services.FilePathService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component //将当前Services变成spring的bean
@Service
public class FilePathServiceImpl implements FilePathService {

    @Autowired
    FilePathMapper filePathMapper;

    @Override
    public Integer addPostFile(FilePath filePath) {
        return filePathMapper.addPostFile(filePath);
    }

    @Override
    public Integer addReplyFile(FilePath filePath) {
        return filePathMapper.addReplyFile(filePath);
    }

    @Override
    public Integer deletePostFile(Long pid) {
        return filePathMapper.deletePostFile(pid);
    }

    @Override
    public Integer deleteRepleFile(Long rid) {
        return filePathMapper.deleteReplyFile(rid);
    }

    @Override
    public Integer deleteFileByFid(Long fid) {
        return filePathMapper.deleteFileByFid(fid);
    }

    @Override
    public FilePath selectFileByNameAndPid(Long pid,String fileName) {
        return filePathMapper.selectFileByNameAndPid(pid, fileName);
    }

    @Override
    public FilePath selectFileByNameAndRid(Long rid,String fileName) {
        return filePathMapper.selectFileByNameAndRid(rid, fileName);
    }

    @Override
    public FilePath selectFileByFid(Long fid) {
        return filePathMapper.selectFileByFid(fid);
    }

    @Override
    public List<FilePath> selectPostFiles(Long pid) {
        return filePathMapper.selectPostFiles(pid);
    }

    @Override
    public List<FilePath> selectReplyFiles(Long rid) {
        return filePathMapper.selectReplyFiles(rid);
    }
}
