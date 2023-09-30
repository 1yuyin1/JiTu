package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout mdrawerLayout;
    private Toolbar mtoolBar;
    private SearchView msearchView;
    private NavigationView mnavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mtoolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mtoolBar);
        msearchView = (SearchView) findViewById(R.id.search_view);
        mnavigationView = findViewById(R.id.nav_view);
        mdrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mdrawerLayout, mtoolBar,
                R.string.drawer_open, R.string.drawer_close);
        mdrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

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
        mnavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

                }
                return true;
            }
        });
    }
}