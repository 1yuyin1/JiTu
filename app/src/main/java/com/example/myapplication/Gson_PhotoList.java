package com.example.myapplication;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Gson_PhotoList
{
    @SerializedName("code")
    private int code;
    public int getCode() { return code; }

    @SerializedName("msg")
    private String msg;
    public String getMsg() { return msg; }

    @SerializedName("data")
    private Data data;
    public Data getData() { return data; }

    static public class Data {
        @SerializedName("records")
        private ArrayList<Gson_Photo> records;
        public ArrayList<Gson_Photo> getRecords() { return records; }

        @SerializedName("total")
        private int total;
        public int getTotal() { return total; }

        @SerializedName("size")
        private int size;
        public int getSize() { return size; }

        @SerializedName("current")
        private int current;
        public int getCurrent() { return current; }
    }
}

