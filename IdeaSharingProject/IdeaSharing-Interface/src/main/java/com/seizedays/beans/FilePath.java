package com.seizedays.beans;

import java.io.Serializable;

public class FilePath implements Serializable {
    private Long fid;
    private Long pid;
    private Long rid;
    private String filePath;
    private String fileName;



    public Long getFid() {
        return fid;
    }

    public void setFid(Long fid) {
        this.fid = fid;
    }

    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    public Long getRid() {
        return rid;
    }

    public void setRid(Long rid) {
        this.rid = rid;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public String toString() {
        return "FilePath{" +
                "fid=" + fid +
                ", pid=" + pid +
                ", rid=" + rid +
                ", filePath='" + filePath + '\'' +
                '}';
    }
}
