package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.NetworkOnMainThreadException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.luck.picture.lib.basic.PictureSelector;
import com.luck.picture.lib.config.SelectMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.interfaces.OnResultCallbackListener;


import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
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

public class AlterActivity extends AppCompatActivity {
    User user;
    private String imagePath =null;
    private String imageUrl;
    private ImageView avatar_image;
    private Button change_avatar_button;
    private Button submit_button;
    private EditText new_uname_edit;
    private EditText new_signature_edit;
    private EditText old_password_edit;
    private EditText confirm_old_password_edit;
    private EditText new_password_edit;
    private Gson gson = new Gson();
    Gson_uploadpicture.class_uploadpicture Info;
    String imageCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alter);

        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("user");

        avatar_image = findViewById(R.id.avatar_image);
        change_avatar_button = findViewById(R.id.change_avatar_button);
        submit_button = findViewById(R.id.submit_button);
        new_uname_edit = findViewById(R.id.new_uname_edit);
        new_signature_edit = findViewById(R.id.new_signature_edit);
        old_password_edit = findViewById(R.id.old_password_edit);
        confirm_old_password_edit = findViewById(R.id.confirm_old_password_edit);


        if(user.getAvatar() != null) {
            Glide.with(AlterActivity.this).load(user.getAvatar()).into(avatar_image);
        }


        change_avatar_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(AlterActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(AlterActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                } else {
                    chooseImg();
                }
            }
        });
        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String new_uname = new_uname_edit.getText().toString().trim();
                String new_signature = new_signature_edit.getText().toString().trim();
                String old_password = old_password_edit.getText().toString().trim();
                String confirm_old_password = confirm_old_password_edit.getText().toString().trim();

                if("".equals(old_password) || "".equals(confirm_old_password)) {
                    Toast.makeText(AlterActivity.this,"请输入旧密码",Toast.LENGTH_SHORT).show();
                } else if (!(old_password.equals(confirm_old_password) )) {
                    Toast.makeText(AlterActivity.this,"两次旧密码不一致",Toast.LENGTH_SHORT).show();
                } else if(old_password.equals(confirm_old_password)) {
                    if(!("".equals(new_uname))) {
                        user.setUsername(new_uname);
                    }
                    if(!("".equals(new_signature))) {
                        user.setIntroduce(new_signature);
                    }

                }
                updateUser();
            }
        });
    }


    private void chooseImg() {
        imagePath = null;
        PictureSelector.create(this)
                .openGallery(SelectMimeType.ofImage())
                .setMaxSelectNum(1)
                .setImageEngine(GlideEngine.createGlideEngine())
                .forResult(new OnResultCallbackListener<LocalMedia>() {
                    @Override
                    public void onResult(ArrayList<LocalMedia> result) {
                        imagePath = result.get(0).getPath();
                        Toast.makeText(AlterActivity.this, imagePath + "路径获取成功", Toast.LENGTH_SHORT).show();
                        uploadImage();
                    }
                    @Override
                    public void onCancel() {
                    }
                });
    }

    private void uploadImage() {
        new Thread(() -> {
            // url路径
            String url = "http://47.107.52.7:88/member/photo/image/upload";
            File file = new File(imagePath);
            // 请求头
            Headers headers = new Headers.Builder()
                    .add("appId", "9434b7cbae6644f4bb2965c9fa46a20a")
                    .add("appSecret", "07005e8f8e038ed074a21a1d44eb2e6d9cc71")
                    .add("Accept", "application/json, text/plain, */*")
                    .build();

            MediaType MEDIA_TYPE_PNG = MediaType.parse("image/*");
            MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");
            MultipartBody multipartBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    // 约定key 如 "certificate" 作为后台接受图片的key；这里约定的key是：certificate
                    .addFormDataPart("fileList",file.getName(),RequestBody.create(MEDIA_TYPE_JSON, file))
                    .build();
            //请求组合创建
            Request request = new Request.Builder()
                    .url(url)
                    // 将请求头加至请求中
                    .headers(headers)
                    .post(multipartBody)
                    .build();
            try {
                OkHttpClient client = new OkHttpClient();
                //发起请求，传入callback进行回调
                client.newCall(request).enqueue(upload_callback);
            }catch (NetworkOnMainThreadException ex){
                ex.printStackTrace();
            }
        }).start();
    }
    private final Callback upload_callback = new Callback() {
        @Override
        public void onFailure(@NonNull Call call, IOException e) {
            //TODO 请求失败处理
            e.printStackTrace();
        }
        @Override
        public void onResponse(@NonNull Call call, Response response) throws IOException {
            String responseData = response.body().string();

            if (response.isSuccessful()) {
                runOnUiThread(() ->
                {
                    Gson_uploadpicture responseParse = gson.fromJson(responseData, Gson_uploadpicture.class);
                    Info = responseParse.getInfo();
                    ArrayList<String> List = Info.getImageUrlList();
                    imageUrl = List.get(0);
                    Glide.with(AlterActivity.this).load(imageUrl).into(avatar_image);
                    Log.d("info", imageUrl);
                });
            }
        }
    };

    // 更新用户信息
    private void updateUser() {
        new Thread(() -> {
            Gson gson=new Gson();
            // url路径
            String url = "http://47.107.52.7:88/member/photo/user/update";
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
            bodyMap.put("avatar", imageUrl);
            bodyMap.put("username",user.getUsername());
            bodyMap.put("introduce",user.getIntroduce());
            bodyMap.put("id","0");
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
                client.newCall(request).enqueue(updater_callback);
            }catch (NetworkOnMainThreadException ex){
                ex.printStackTrace();
            }
        }).start();
    }
    private final Callback updater_callback = new Callback() {
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
            // 解析json串到自己封装的状态
            ResponseBody<String> dataResponseBody = gson.fromJson(body, jsonType);
            Log.d("info", dataResponseBody.toString());
            Intent intent = new Intent(AlterActivity.this, MainActivity.class);
            intent.putExtra("user",dataResponseBody.toString());
            startActivity(intent);
            finishAffinity(); // 清除当前活动栈，避免返回到登录界面
        }
    };
}