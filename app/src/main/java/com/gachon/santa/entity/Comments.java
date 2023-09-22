package com.gachon.santa.entity;

import java.util.Date;

public class Comments {
    private String pid;
    private String publisherId;
    private String content;
    private String url;
    private Date createdAt;

    public Comments(String pid, String publisherId, String content, String url, Date createdAt) {
        this.pid = pid;
        this.publisherId = publisherId;
        this.content = content;
        this.url = url;
        this.createdAt = createdAt;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
