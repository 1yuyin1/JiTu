package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class RegisterActivity extends AppCompatActivity {
    private Button ReButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ReButton = (Button) findViewById(R.id.btn_register);

        ReButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //将账号信息加入到数据库
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finishAffinity(); // 清除当前活动栈，避免返回到登录界面
            }
        });
    }
}