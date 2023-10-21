package com.example.myapplication;

import com.google.gson.annotations.SerializedName;

public class Gson_User
{
    @SerializedName("id")//主键id
    private String mId;
    public String getId() {
        return mId;
    }

    @SerializedName("appKey")//用户创建应用的id
    private String appKey;
    public String getAppKey() {
        return appKey;
    }

    @SerializedName("username") //用户名
    private String username;
    public String getUsername() {
        return username;
    }

    @SerializedName("password")//密码
    private String password;
    public String getPassword() {
        return password;
    }

    @SerializedName("sex")//性别
    private int sex;
    public int getSex() {
        return sex;
    }

    @SerializedName("introduce")//个人介绍
    private String introduce;
    public String getIntroduce() {
        return introduce;
    }

    @SerializedName("avatar")//头像
    private String avatar;
    public String getAvatar() {
        return avatar;
    }

    @SerializedName("createTime")//创建时间
    private String createTime;
    public String getCreateTime() {
        return createTime;
    }

    @SerializedName("lastUpdateTime")//修改时间
    private String lastUpdateTime;
    public String getLastUpdateTime() {
        return lastUpdateTime;
    }
}

