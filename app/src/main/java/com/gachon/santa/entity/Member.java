package com.gachon.santa.entity;

import java.io.Serializable;

public class Member implements Serializable {

    private String uid;
    private String name;
    private String age;
    private String sex;
    private String address;
    private String type = "user";

    public Member(String uid, String name, String age, String sex, String address) {
        this.uid = uid;
        this.name = name;
        this.age = age;
        this.sex = sex;
        this.address = address;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
