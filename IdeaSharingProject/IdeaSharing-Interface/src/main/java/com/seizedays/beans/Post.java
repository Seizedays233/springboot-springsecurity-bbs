package com.seizedays.beans;

import java.io.Serializable;
import java.util.Date;

public class Post implements Serializable {
    private Long pid;
    private String ptitle;
    private String pbody;
    private Long replyCount;    //帖子回复数
    //    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date psendTime;
    private IdeaUser ideaUser;
    private Date lastReplyTime;//最后回复时间
    private Integer tempSave;

    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    public String getPtitle() {
        return ptitle;
    }

    public void setPtitle(String ptitle) {
        this.ptitle = ptitle;
    }

    public String getPbody() {
        return pbody;
    }

    public void setPbody(String pbody) {
        this.pbody = pbody;
    }

    public Long getReplyCount() {
        return replyCount;
    }

    public void setReplyCount(Long replyCount) {
        this.replyCount = replyCount;
    }

    public Date getPsendTime() {
        return psendTime;
    }

    public void setPsendTime(Date psendTime) {
        this.psendTime = psendTime;
    }

    public IdeaUser getIdeaUser() {
        return ideaUser;
    }

    public void setIdeaUser(IdeaUser ideaUser) {
        this.ideaUser = ideaUser;
    }

    public Date getLastReplyTime() {
        return lastReplyTime;
    }

    public void setLastReplyTime(Date lastReplyTime) {
        this.lastReplyTime = lastReplyTime;
    }

    public Integer getTempSave() {
        return tempSave;
    }

    public void setTempSave(Integer tempSave) {
        this.tempSave = tempSave;
    }


    @Override
    public String toString() {
        return "Post{" +
                "pid=" + pid +
                ", ptitle='" + ptitle + '\'' +
                ", pbody='" + pbody + '\'' +
                ", replyCount=" + replyCount +
                ", psendTime=" + psendTime +
                ", ideaUser=" + ideaUser +
                ", lastReplyTime=" + lastReplyTime +
                ", tempSave=" + tempSave +
                '}';
    }
}