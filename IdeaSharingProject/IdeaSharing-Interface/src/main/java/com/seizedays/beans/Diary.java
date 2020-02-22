package com.seizedays.beans;

import java.io.Serializable;

public class Diary implements Serializable {
    private Long did;
    private String date;
    private String content;
    private String imageName;
    private Long uid;

    public Long getDid() {
        return did;
    }

    public void setDid(Long did) {
        this.did = did;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    @Override
    public String toString() {
        return "Diary{" +
                "did=" + did +
                ", date='" + date + '\'' +
                ", content='" + content + '\'' +
                ", imageName='" + imageName + '\'' +
                ", uid=" + uid +
                '}';
    }
}
