package com.example.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
    public PictureAdapter(List<Picture> pictureList){
        mPictureList = pictureList;
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
        holder.image_nameView.setText(picture.image_name);
        holder.usnView.setText(picture.usn);
        holder.praiseView.setText(String.valueOf(picture.praise));
        String comment ="";
        for (int i = 0; i < picture.commentList.size(); i++) {
            comment += picture.commentList.get(i).commenter;
            comment += ":";
            comment += picture.commentList.get(i).content;
            comment += "。";

        }
        holder.comView.setText(comment);
        //这是通过file加载，也可以通过url加载（需要上传至服务器）
//        Bitmap bitmap = BitmapFactory.decodeFile(picture.image.getLocalFile().getAbsolutePath());
//        Glide.with(mContext).load(bitmap);

        Glide.with(mContext).load(picture.image.getFileUrl()).into(holder.pictureView);
    }
    public int getItemCount(){
        return mPictureList.size();
    }
}
