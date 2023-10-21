package com.example.myapplication;

import com.google.gson.annotations.SerializedName;

public class Gson_Photo
{
    @SerializedName("id")
    private String Id;
    public String getId() {
        return Id;
    }

    @SerializedName("pUserId")
    private String pUserId;
    public String getPUserId() {
        return pUserId;
    }

    @SerializedName("imageCode")
    private String imageCode;
    public String getImageCode() {
        return imageCode;
    }

    @SerializedName("title")
    private String title;
    public String getTitle() {
        return title;
    }

    @SerializedName("content")
    private String content;
    public String getContent() {
        return content;
    }

    @SerializedName("createTime")
    private String createTime;
    public String getCreateTime() {
        return createTime;
    }

    @SerializedName("imageUrlList")
    private String[] imageUrlList;
    public String[] getImageUrlList() {
        return imageUrlList;
    }

    @SerializedName("likeId")
    private String likeId;
    public String getLikeId() {
        return likeId;
    }

    @SerializedName("likeNum")
    private String likeNum;
    public String getLikeNum() {
        return likeNum;
    }

    @SerializedName("hasLike")
    private boolean hasLike;
    public boolean getHasLike() {
        return hasLike;
    }

    public void setHasLike(boolean hasLike) {
        this.hasLike = hasLike;
    }

    @SerializedName("collectId")
    private String collectId;
    public String getCollectId() {
        return collectId;
    }

    @SerializedName("collectNum")
    private String collectNum;
    public String getCollectNum() {
        return collectNum;
    }

    @SerializedName("hasCollect")
    private boolean hasCollect;
    public boolean getHasCollect() {
        return hasCollect;
    }

    public void setHasCollect(boolean hasCollect){
        this.hasCollect = hasCollect;
    }

    @SerializedName("username")
    private String username;
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @SerializedName("hasFocus")
    private boolean hasFocus;

    public boolean getHasFocus() {
        return hasFocus;
    }

    public void setHasFocus(boolean hasFocus) {
        this.hasFocus = hasFocus;
    }
}
