package com.example.myapplication;

import com.google.gson.annotations.SerializedName;

public class Persons
{
    @SerializedName("code")
    private int code;
    public int getCode() { return code; }

    @SerializedName("msg")
    private String msg;
    public String getMsg() { return msg; }

    @SerializedName("data")
    private Gson_User user;
    public Gson_User getUser() { return user; }
}



//{
//        msg:"string"
//        code:0
//        data:{
//        appKey:"string"
//        avatar:"string"
//        createTime:0
//        id:0
//        introduce:"string"
//        lastUpdateTime:0
//        password:"string"
//        sex:0
//        username:"string"
//        }
//        }
