package com.seizedays.beans;

import java.io.Serializable;
import java.util.Date;

public class Reply implements Serializable {
    private IdeaUser ideaUser;
    private Post post;
    private String replyMessage;
    private Date replyTime;
    private Long rid;

    public IdeaUser getIdeaUser() {
        return ideaUser;
    }

    public void setIdeaUser(IdeaUser ideaUser) {
        this.ideaUser = ideaUser;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public String getReplyMessage() {
        return replyMessage;
    }

    public void setReplyMessage(String replyMessage) {
        this.replyMessage = replyMessage;
    }

    public Date getReplyTime() {
        return replyTime;
    }

    public void setReplyTime(Date replyTime) {
        this.replyTime = replyTime;
    }

    public Long getRid() {
        return rid;
    }

    public void setRid(Long rid) {
        this.rid = rid;
    }

    @Override
    public String toString() {
        return "Reply{" +
                "ideaUser=" + ideaUser +
                ", post=" + post +
                ", replyMessage='" + replyMessage + '\'' +
                ", replyTime=" + replyTime +
                ", rid=" + rid +
                '}';
    }
}
