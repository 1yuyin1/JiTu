package com.example.myapplication;



import java.util.ArrayList;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

public class Picture extends BmobObject {

    public String url;
    public String shareId;
    public String usn = "";
    public String usId ="";
    public String imageName ="";
    public String content="1";
    public String imageCode = "";
    public String praise = "";
    public ArrayList<Comment> commentList = new ArrayList<>();
    static class Comment{
            public String commenter;  // 评论者
            public String content;    // 评论内容
    }

    public Picture(String imageCode,String usId, String image_name) {
        this.usId = usId;
        this.imageName = image_name;
        this.imageCode = imageCode;
    }
}
