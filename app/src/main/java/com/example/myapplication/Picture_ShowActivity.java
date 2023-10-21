package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.NetworkOnMainThreadException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Picture_ShowActivity extends AppCompatActivity {
    private Gson gson = new Gson();
    private Picture picture;
    private User user;
    private ImageView ImageView;
    private TextView ImageNameView;
    private TextView UsNameView;
    private TextView PraiseView;
    private TextView CommentView;
    private EditText comment;
    private Button Submit;
    private FloatingActionButton downLoad;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_show);
        initView();

        //点赞活动
        ImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                likePost();
            }
        });
        //提交至服务器
        Submit.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View view) {

                                      }
                                  });
        //将图片下载至本地
        downLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }
    private void initView(){
        ImageView = findViewById(R.id.ImageView);
        ImageNameView = findViewById(R.id.ImageNameView);
        UsNameView = findViewById(R.id.UsNameView);
        PraiseView = findViewById(R.id.PraiseView);
        CommentView = findViewById(R.id.CommentView);
        comment = findViewById(R.id.WriteCommentView);
        Submit = findViewById(R.id.SubmitButton);
        downLoad = findViewById(R.id.downLoad);
        Intent intent = getIntent();
        picture = (Picture) intent.getSerializableExtra("picture");
        user = (User) intent.getSerializableExtra("user");

        //加载图片
        Glide.with(Picture_ShowActivity.this).load(picture.url).into(ImageView);
        ImageNameView.setText("图片名称：" + picture.imageName);
        UsNameView.setText("图片上传者：" + picture.usn);
        PraiseView.setText(String.valueOf("点赞数：" +picture.praise));
        String comment ="";
        for (int i = 0; i < picture.commentList.size(); i++) {
            comment += picture.commentList.get(i).commenter;
            comment += ":";
            comment += picture.commentList.get(i).content;
            comment += "\n";
        }
        CommentView.setText(comment);
    }
    private void comment_post(){
        new Thread(() -> {
            // url路径
            String url = "http://47.107.52.7:88/member/photo/comment/first";

            // 请求头
            Headers headers = new Headers.Builder()
                    .add("Accept", "application/json, text/plain, */*")
                    .add("appId", "9434b7cbae6644f4bb2965c9fa46a20a")
                    .add("appSecret", "07005e8f8e038ed074a21a1d44eb2e6d9cc71")
                    .add("Content-Type", "application/json")
                    .build();

            // 请求体
            // PS.用户也可以选择自定义一个实体类，然后使用类似fastjson的工具获取json串
            Map<String, Object> bodyMap = new HashMap<>();
            bodyMap.put("shareId", picture.shareId);
            bodyMap.put("userName", user.getUsername());
            bodyMap.put("userId", user.getUsId());
            bodyMap.put("content", comment.getText().toString().trim());
            // 将Map转换为字符串类型加入请求体中
            String body = gson.toJson(bodyMap);
            MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");
            //请求组合创建
            Request request = new Request.Builder()
                    .url(url)
                    // 将请求头加至请求中
                    .headers(headers)
                    .post(RequestBody.create(MEDIA_TYPE_JSON, body))
                    .build();
            try {
                OkHttpClient client = new OkHttpClient();
                //发起请求，传入callback进行回调
                client.newCall(request).enqueue(callback);
            }catch (NetworkOnMainThreadException ex){
                ex.printStackTrace();
            }
        }).start();
    }

    /**
     * 回调
     */
    private final Callback callback = new Callback() {
        @Override
        public void onFailure(@NonNull Call call, IOException e) {
            //TODO 请求失败处理
            e.printStackTrace();
        }
        @Override
        public void onResponse(@NonNull Call call, Response response) throws IOException {
            //TODO 请求成功处理
            Type jsonType = new TypeToken<ResponseBody<Object>>(){}.getType();
            // 获取响应体的json串
            String body = response.body().string();
            Log.d("info", body);
            // 解析json串到自己封装的状态
            ResponseBody<Object> dataResponseBody = gson.fromJson(body,jsonType);
            Log.d("info", dataResponseBody.toString());
        }
    };

    private void likePost() {
        new Thread(() -> {
            // url路径
            String url = "http://47.107.52.7:88/member/photo/like?shareId=" + picture. shareId + "&userId=" + picture.usId;
            // 请求头
            Headers headers = new Headers.Builder()
                    .add("appId", "9434b7cbae6644f4bb2965c9fa46a20a")
                    .add("appSecret", "07005e8f8e038ed074a21a1d44eb2e6d9cc71")
                    .add("Accept", "application/json, text/plain, */*")
                    .build();

            MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");

            //请求组合创建
            Request request = new Request.Builder()
                    .url(url)
                    .headers(headers)
                    .post(RequestBody.create(MEDIA_TYPE_JSON, ""))
                    .build();
            try {
                OkHttpClient client = new OkHttpClient();
                //发起请求，传入callback进行回调
                client.newCall(request).enqueue(likepostcallback);
            } catch (NetworkOnMainThreadException ex) {
                ex.printStackTrace();
            }
        }).start();
    }
    private final Callback likepostcallback = new Callback() {
        @Override
        public void onFailure(@NonNull Call call, IOException e) {
            //TODO 请求失败处理
            e.printStackTrace();
        }

        @Override
        public void onResponse(@NonNull Call call, Response response) throws IOException {
            //TODO 请求成功处理
            Type jsonType = new TypeToken<ResponseBody<Object>>() {
            }.getType();
            // 获取响应体的json串
            String body = response.body().string();
            Log.d("info", body);
        }
    };


}