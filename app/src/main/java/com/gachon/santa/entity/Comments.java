package com.gachon.santa.entity;

import java.util.Date;

public class Comments {
    private String cid;
    private String pid;
    private String publisherId;
    private String content;
    private Date createdAt;

    public Comments(String cid, String pid, String publisherId, String content, Date createdAt) {
        this.cid = cid;
        this.pid = pid;
        this.publisherId = publisherId;
        this.content = content;
        this.createdAt = createdAt;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getPublisherId() {
        return publisherId;
    }

    public void setPublisherId(String publisherId) {
        this.publisherId = publisherId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
