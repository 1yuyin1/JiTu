package com.example.myapplication;



import java.util.ArrayList;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

public class Picture extends BmobObject {
    public BmobFile image;
    public String usn ="";
    public String image_name ="";
    public int praise = 0;
    public ArrayList<Comment> commentList = new ArrayList<>();
    static class Comment{
            public String commenter;  // 评论者
            public String content;    // 评论内容
    }
    public Picture(BmobFile image, String usn, String image_name, int praise) {
        this.image = image;
        this.usn = usn;
        this.image_name = image_name;
        this.praise = praise;
    }
}
