package com.example.myapplication;


import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;


import android.content.Intent;

import android.os.Bundle;


import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;


public class MainActivity extends AppCompatActivity {
    public String imagePath;
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
                Toast.makeText(MainActivity.this, imagePath +"上传成功", Toast.LENGTH_SHORT).show();
                //Toast.makeText(MainActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
                //从相册中获取图片路径
            }
        });
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
                            Toast.makeText(MainActivity.this, "退出成功", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(MainActivity.this, LoginActivity.class));
                            finishAffinity(); // 清除当前活动栈，避免返回到登录界面
                    }
                    // 返回 true 表示已经处理了菜单项的点击事件
                    return true;
                }
            });
        }
    }
    private void initView(){
        mtoolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mtoolBar);
        msearchView = (SearchView) findViewById(R.id.search_view);
        mnavigationView = findViewById(R.id.nav_view);
        mdrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        mfab = (FloatingActionButton)findViewById(R.id.upload);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mdrawerLayout, mtoolBar,
                R.string.drawer_open, R.string.drawer_close);
        mdrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0); // 获取 headerLayout 布局的根视图
        TextView headerTextview = headerView.findViewById(R.id.username); // 找到 TextView
        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("user");
        headerTextview.setText(user.getUsername());
    }

}