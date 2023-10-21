package com.example.myapplication;


import androidx.annotation.NonNull;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.Manifest;

import android.content.Intent;

import android.content.pm.PackageManager;

import android.os.Bundle;

import android.os.NetworkOnMainThreadException;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;


import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.luck.picture.lib.basic.PictureSelector;
import com.luck.picture.lib.config.SelectMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.interfaces.OnResultCallbackListener;


import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity {

    private List<Picture> pictureList = new ArrayList<>();
    private PictureAdapter adapter;
    private String imagePath;
    private DrawerLayout mdrawerLayout;
    private Toolbar mtoolBar;
    private SearchView msearchView;
    private RecyclerView mrecyclerView;
    private NavigationView mnavigationView;
    private User user = new User();
    private FloatingActionButton mfab;
    private final Gson gson = new Gson();
    Map<String,String> dataMap;

    Gson_uploadpicture.class_uploadpicture Info;
    String imageCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

        msearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // 处理搜索提交事件
                for (int i = 0; i < pictureList.size(); i++) {
                    if ((pictureList.get(i).imageName).equals(query)) {
                        //显示图片
                        Intent intent = new Intent(MainActivity.this, Picture_ShowActivity.class);
                        intent.putExtra("picture", pictureList.get(i));
                        intent.putExtra("user", user);
                        startActivity(intent);
                    }
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // 处理搜索文本变化事件
                return true;
            }
        });
        //上传图片功能
        mfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "上传图片", Toast.LENGTH_SHORT).show();
                //从相册中获取图片路径，暂时只能一张
                //请求权限
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                } else {
                    chooseImg();
                }
            }
        });
        //抽屉导航
        Menu menu = mnavigationView.getMenu();
        for (int i = 0; i < menu.size(); i++) {
            MenuItem menuItem = menu.getItem(i);
            menuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    // 处理菜单项的点击事件
                    int itemId = item.getItemId();
                    if (itemId == R.id.nav_logout) {
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                        finishAffinity(); // 清除当前活动栈，避免返回到登录界面
                    } else if (itemId == R.id.nav_alter || itemId == R.id.nav_signature || itemId == R.id.nav_usna) {
                        Intent intent = new Intent(MainActivity.this, AlterActivity.class);
                        intent.putExtra("user", user);
                        startActivity(intent);
                    } else if (itemId == R.id.nav_about) {
                        Toast.makeText(MainActivity.this, "欢迎使用集图软件", Toast.LENGTH_SHORT).show();
                    }
                    // 返回 true 表示已经处理了菜单项的点击事件
                    return true;
                }
            });
        }
    }

    private void initView() {
        mtoolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mtoolBar);
        msearchView = (SearchView) findViewById(R.id.search_view);
        mnavigationView = findViewById(R.id.nav_view);
        mdrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        mfab = (FloatingActionButton) findViewById(R.id.upload);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mdrawerLayout, mtoolBar,
                R.string.drawer_open, R.string.drawer_close);
        mdrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        mnavigationView = findViewById(R.id.nav_view);
        View headerView = mnavigationView.getHeaderView(0); // 获取 headerLayout 布局的根视图
        ImageView avater = headerView.findViewById(R.id.avatar_image);
        TextView uname = headerView.findViewById(R.id.username); // 找到 用户名的TextView
        TextView sign = headerView.findViewById(R.id.signature);
        Intent intent = getIntent();
        String a = (String) intent.getSerializableExtra("user");
        Gson_User useData = gson.fromJson(a,Gson_User.class);

        user.setUsername(useData.getUsername());
        user.setIntroduce(useData.getIntroduce());
        user.setUsId(useData.getId());
        uname.setText(user.getUsername());
        sign.setText(user.getIntroduce());
        if(user.getAvatar() != null){
            Glide.with(MainActivity.this).load(user.getAvatar()).into(avater);
        }


        initpictures();
        mrecyclerView = (RecyclerView) findViewById(R.id.recycler);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        mrecyclerView.setLayoutManager(layoutManager);
        adapter = new PictureAdapter(pictureList, user);
        mrecyclerView.setAdapter(adapter);
    }

    private void initpictures() {
        pictureList.clear();
        get(user.getUsId());
    }


    //请求权限的回调函数
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    chooseImg();
                } else {
                    Toast.makeText(this, "You denied the permission", Toast.LENGTH_SHORT).show();
                }
                break;
            case 2:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    uploadImage();
                } else {
                    Toast.makeText(this, "You denied the permission", Toast.LENGTH_SHORT).show();
                }
        }
    }

    //调用picture selector并返回路径
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
                        //Toast.makeText(MainActivity.this, imagePath + "路径获取成功", Toast.LENGTH_SHORT).show();
                        //请求权限
                        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 2);
                        } else {
                            uploadImage();
                        }
                    }

                    @Override
                    public void onCancel() {

                    }
                });
    }


    // 将图片上传至 服务器
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
                    imageCode = Info.getImageCode();
                    String fileName = imagePath.substring(imagePath.lastIndexOf('/') + 1); // 获取文件名称（带后缀）
                    String name = fileName.substring(0, fileName.lastIndexOf('.')); // 去除后缀
                    Log.d("info", imageCode + " " + user.getUsId() + " " +name);
                    share(new Picture(imageCode,user.getUsId(),name));
                });
            }
        }
    };

    private void share(Picture picture)
    {
        new Thread(() -> {
            // url路径
            String url = "http://47.107.52.7:88/member/photo/share/add";
            // 请求头
            Headers headers = new Headers.Builder()
                    .add("appId", "9434b7cbae6644f4bb2965c9fa46a20a")
                    .add("appSecret", "07005e8f8e038ed074a21a1d44eb2e6d9cc71")
                    .add("Accept", "application/json, text/plain, */*")
                    .build();
            // 请求体
            // PS.用户也可以选择自定义一个实体类，然后使用类似fastjson的工具获取json串
            Map<String, Object> bodyMap = new HashMap<>();
            bodyMap.put("content", picture.content);
            bodyMap.put("imageCode", picture.imageCode);
            bodyMap.put("pUserId", picture.usId);
            bodyMap.put("title", picture.imageName);
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
                client.newCall(request).enqueue(addcallback);
            }catch (NetworkOnMainThreadException ex){
                ex.printStackTrace();
            }
        }).start();
    }

    private final Callback addcallback = new Callback() {
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
            Log.d("share", dataResponseBody.toString());
        }
    };

    private void getUser(String username) {
        new Thread(() -> {
            // url路径
            String url = "http://47.107.52.7:88/member/photo/user/getUserByName?username=" + username;
            // 请求头
            Headers headers = new Headers.Builder()
                    .add("Accept", "application/json, text/plain, */*")
                    .add("appId", "9434b7cbae6644f4bb2965c9fa46a20a")
                    .add("appSecret", "07005e8f8e038ed074a21a1d44eb2e6d9cc71")
                    .build();

            //请求组合创建
            Request request = new Request.Builder()
                    .url(url)
                    // 将请求头加至请求中
                    .headers(headers)
                    .get()
                    .build();
            try {
                OkHttpClient client = new OkHttpClient();
                //发起请求，传入callback进行回调
                client.newCall(request).enqueue(callback);
            } catch (NetworkOnMainThreadException ex) {
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
            Type jsonType = new TypeToken<ResponseBody<Object>>() {
            }.getType();
            // 获取响应体的json串
            String body = response.body().string();
            Log.d("info", body);
            // 解析json串到自己封装的状态
            ResponseBody<String> dataResponseBody = gson.fromJson(body, jsonType);
            Log.d("add", dataResponseBody.toString());
        }
    };

    private void getComment(){
        new Thread(() -> {

            // url路径
            String url = "http://47.107.52.7:88/member/photo/comment/first?current=0&size=0&shareId=0";

            // 请求头
            Headers headers = new Headers.Builder()
                    .add("Accept", "application/json, text/plain, */*")
                    .add("appId", "9434b7cbae6644f4bb2965c9fa46a20a")
                    .add("appSecret", "07005e8f8e038ed074a21a1d44eb2e6d9cc71")
                    .build();

            //请求组合创建
            Request request = new Request.Builder()
                    .url(url)
                    // 将请求头加至请求中
                    .headers(headers)
                    .get()
                    .build();
            try {
                OkHttpClient client = new OkHttpClient();
                //发起请求，传入callback进行回调
                client.newCall(request).enqueue(comment_callback);
            }catch (NetworkOnMainThreadException ex){
                ex.printStackTrace();
            }
        }).start();
    }
    private final Callback comment_callback = new Callback() {
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

    private void get(String userId) {
        new Thread(() -> {
            // url路径
            String url = "http://47.107.52.7:88/member/photo/share?userId=";

            // 请求头
            Headers headers = new Headers.Builder()
                    .add("appId", "9434b7cbae6644f4bb2965c9fa46a20a")
                    .add("appSecret", "07005e8f8e038ed074a21a1d44eb2e6d9cc71")
                    .add("Accept", "application/json, text/plain, */*")
                    .build();
            //请求组合创建
            Request request = new Request.Builder()
                    .url(url + userId)
                    // 将请求头加至请求中
                    .headers(headers)
                    .get()
                    .build();
                try {
                    OkHttpClient client = new OkHttpClient();
                    //发起请求，传入callback进行回调
                    client.newCall(request).enqueue(Getcallback);
                } catch (NetworkOnMainThreadException ex) {
                    ex.printStackTrace();
            }
        }).start();
    }
    private final Callback Getcallback = new Callback() {
        @Override
        public void onFailure(@NonNull Call call, IOException e) {
            e.printStackTrace();
        }

        @Override
        public void onResponse(@NonNull Call call, Response response) throws IOException {
            if (response.isSuccessful()) {
                String responseBody = response.body().string();
                Log.d("图片发现", "响应体 : " + responseBody);
                addPhotoList(responseBody);
            }

        }
    };

    private void addPhotoList(String body) {
        new Thread(() -> {
            Gson_PhotoList responseParse = gson.fromJson(body, Gson_PhotoList.class);
            if (responseParse.getData() != null){
                ArrayList<Gson_Photo> photoList = responseParse.getData().getRecords();
                for (Gson_Photo photo : photoList) {
                    String[] List = photo.getImageUrlList();
                    for (int i = 0; i < List.length; i++) {
                        Picture picture = new Picture(photo.getImageCode(),photo.getPUserId(),photo.getTitle());
                        picture.shareId = photo.getId();
                        picture.usId  = photo.getPUserId();
                        picture.usn = photo.getUsername();
                        picture.praise = photo.getLikeNum();
                        picture.url = List[i];
                        pictureList.add(picture);
                        Log.d("LOG", List[i]);
                    }
              }
            }
        }).start();
    }

}