package com.netease.comple.contentsale.entity;

public class User {

    private int id;
    private String userName;
    private String nickName;
    private String password;
    private int userType;

    public int getId(){ return id;}

    public void setId(int id){ this.id = id;}

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getNickName(){return nickName;}

    public void setNickName(String nickName){this.nickName = nickName;}

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }
}
