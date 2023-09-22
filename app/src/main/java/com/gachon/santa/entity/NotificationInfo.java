package com.gachon.santa.entity;

import java.io.Serializable;
import java.util.Date;

public class NotificationInfo implements Serializable {

    private String cid;
    private String publisherId;
    private String uid;
    private String pid;
    private Date time;

    public NotificationInfo(String cid, String publisherId, String uid, String pid, Date time) {
        this.cid = cid;
        this.publisherId = publisherId;
        this.uid = uid;
        this.pid = pid;
        this.time = time;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getPublisherId() {
        return publisherId;
    }

    public void setPublisherId(String publisherId) {
        this.publisherId = publisherId;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
