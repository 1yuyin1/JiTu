package com.example.myapplication;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Gson_uploadpicture
{
    @SerializedName("code")
    private int code;
    public int getCode() { return code; }

    @SerializedName("msg")
    private String msg;
    public String getMsg() { return msg; }

    @SerializedName("data")
    private class_uploadpicture Info;
    public class_uploadpicture getInfo() { return Info;}


    public  class class_uploadpicture
    {
        private String imageCode;
        public String getImageCode(){return imageCode;}
        public ArrayList<String> imageUrlList;

        public ArrayList<String> getImageUrlList() {
            return imageUrlList;
        }
    }
}
