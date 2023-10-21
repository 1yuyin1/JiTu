package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;

import java.util.List;

public class PictureAdapter extends RecyclerView.Adapter<PictureAdapter.ViewHolder> {
    private User muser;
    private Context mContext;
    private List<Picture> mPictureList;
    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView pictureView;
        TextView image_nameView;
        TextView usnView;
        TextView praiseView;
        TextView comView;
        public ViewHolder(View view){
            super(view);
            cardView = (CardView) view;
            pictureView = view.findViewById(R.id.image);
            image_nameView = view.findViewById(R.id.image_name);
            usnView = view.findViewById(R.id.usn);
            praiseView = view.findViewById(R.id.praise);
            comView = view.findViewById(R.id.comment);

        }
    }
    public PictureAdapter(List<Picture> pictureList,User user){
        mPictureList = pictureList;
        muser = user;
    }
    public ViewHolder onCreateViewHolder(ViewGroup parent,int viewTtpe){
        if ((mContext == null)) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.image_item,parent,false);
        return new ViewHolder(view);
    }
    public void onBindViewHolder(ViewHolder holder,int position) {
        Picture picture = mPictureList.get(position);

        //设置textview内容
        holder.image_nameView.setText(picture.imageName);
        holder.usnView.setText(picture.usn);
        holder.praiseView.setText(String.valueOf(picture.praise));
        String comment ="";
        //当评论List不为空时，只添加上一个评论内容，以防过多
        if(!picture.commentList.isEmpty()) {
            comment += picture.commentList.get(0).commenter;
            comment += ":";
            comment += picture.commentList.get(0).content;
            comment += "。";
        }
        holder.comView.setText(comment);

        //通过url加载图片
        Glide.with(mContext).load(picture.url).into(holder.pictureView);
        holder.pictureView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext,Picture_ShowActivity.class);
                intent.putExtra("picture",picture);
                intent.putExtra("user",muser);
                mContext.startActivity(intent);
            }
        });
    }
    public int getItemCount(){
        return mPictureList.size();
    }
}
