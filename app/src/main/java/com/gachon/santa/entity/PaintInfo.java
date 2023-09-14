package com.gachon.santa.entity;

import java.util.Date;

public class PaintInfo {

    String pid;
    String uid;
    String url;
    String type;
    Date date;

    public PaintInfo(String pid, String uid, String url, String type, Date date) {
        this.pid = pid;
        this.uid = uid;
        this.url = url;
        this.type = type;
        this.date = date;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
