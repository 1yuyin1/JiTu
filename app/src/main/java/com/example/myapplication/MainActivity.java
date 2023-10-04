package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import android.annotation.SuppressLint;
import android.content.Intent;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.luck.picture.lib.basic.PictureSelector;
import com.luck.picture.lib.config.SelectMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.interfaces.OnResultCallbackListener;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;


public class MainActivity extends AppCompatActivity {
    private Picture[] pictures = {
                new Picture(new BmobFile("111.jpg","","https://tupian.qqw21.com/article/UploadPic/2020-6/2020612391694489.jpg"),"123","add",1),
                new Picture(new BmobFile("222.jpg","","https://tupian.qqw21.com/article/UploadPic/2020-4/20204823154758937.jpg"),"123","add",2),
                new Picture(new BmobFile("333.jpg","","https://p.qqan.com/up/2021-1/16104196979967970.jpg"),"123","add",3),
            new Picture(new BmobFile("444.jpg","","https://tupian.qqw21.com/article/UploadPic/2020-5/20205515493278292.jpg"),"123","add",4),
            new Picture(new BmobFile("555.jpg","","https://p.qqan.com/up/2018-5/2018050911304322378.jpg"),"123","add",5)};
    private List<Picture> pictureList = new ArrayList<>();
    private PictureAdapter adapter;
    private String imagePath;
    private DrawerLayout mdrawerLayout;
    private Toolbar mtoolBar;
    private SearchView msearchView;
    private NavigationView mnavigationView;
    private User user;
    private FloatingActionButton mfab;
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
                    if((pictureList.get(i).image_name).equals(query)) {
                        //显示图片
                        Toast.makeText(MainActivity.this, "查找成功", Toast.LENGTH_SHORT).show();
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
        //悬浮菜单
        Menu menu = mnavigationView.getMenu();
        for (int i = 0; i < menu.size(); i++) {
            MenuItem menuItem = menu.getItem(i);
            menuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    // 处理菜单项的点击事件
                    int itemId = item.getItemId();
                    if(itemId == R.id.nav_logout){
                            user.logOut();
                            startActivity(new Intent(MainActivity.this, LoginActivity.class));
                            finishAffinity(); // 清除当前活动栈，避免返回到登录界面
                            Toast.makeText(MainActivity.this, "退出成功", Toast.LENGTH_SHORT).show();
                    }else if(itemId == R.id.nav_alter || itemId == R.id.nav_signature || itemId == R.id.nav_usna){
                        Intent intent = new Intent(MainActivity.this, AlterActivity.class);
                        intent.putExtra("user",user);
                        startActivity(intent);
                        finishAffinity(); // 清除当前活动栈，避免返回到登录界面
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

        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0); // 获取 headerLayout 布局的根视图
        TextView usname = headerView.findViewById(R.id.username); // 找到 用户名的TextView
        TextView sign = headerView.findViewById(R.id.signature);
        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("user");
        usname.setText(user.getUsername());
        sign.setText(user.getSignature());

        initpictures();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler);
        GridLayoutManager layoutManager = new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new PictureAdapter(pictureList);
        recyclerView.setAdapter(adapter);

        Bmob.initialize(this, "f8a586e2cddf826ad840f9f072728ef6");
    }
    private  void initpictures() {
        pictureList.clear();
        //loadImage()应该从服务器加载，但是由于无法连接，故使用本地
        //由于图片数量较少，采取循环方式展示
        for (int i = 0; i < 50; i++) {
            Random random = new Random();
            int index = random.nextInt(pictures.length);
            pictureList.add(pictures[index]);
        }
    }

    private void loadImage() {
//        BmobQuery<BmobObject> query = new BmobQuery<>("Image");
//        query.findObjects(new FindListener<BmobObject>() {
//            @Override
//            public void done(List<BmobObject> objectList, BmobException e) {
//                if (e == null) {
//                    for (BmobObject object : objectList) {
////                        pictureList.add();
//                    }
//                } else {
//                    // 查询失败
//                }
//            }
//        });
    }
    //请求权限的回调函数
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    chooseImg();
                } else {
                    Toast.makeText(this,"You denied the permission",Toast.LENGTH_SHORT).show();
                }
                break;
            case 2:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    uploadImage(imagePath);
                } else {
                    Toast.makeText(this,"You denied the permission",Toast.LENGTH_SHORT).show();
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
                            uploadImage(imagePath);
                        }
                    }
                    @Override
                    public void onCancel() {

                    }
                });
    }
    // 将图片上传至 Bmob 服务器
    private void uploadImage(String imagePath) {
        File file = new File(imagePath);
        Picture picture = new Picture(new BmobFile(file),user.getUsername(), file.getName(), 0);
        picture.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if(e==null){
                    Toast.makeText(MainActivity.this, "上传成功!", Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    Toast.makeText(MainActivity.this, "上传失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}