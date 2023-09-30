package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {
    private Button mButton;
    private EditText username;
    private EditText pass;
    private TextView register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mButton = findViewById(R.id.loginButton);
        username = findViewById(R.id.usernameEditText);
        pass = findViewById(R.id.passwordEditText);
        register = findViewById(R.id.registerTextView);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 进行登录验证
                if (loginSuccessful()) {
                    // 登录成功，跳转到登录成功后的界面
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finishAffinity(); // 清除当前活动栈，避免返回到登录界面
                } else {
                    // 登录失败，显示错误提示信息
                }
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
                finishAffinity();
            }
        });

    }
    public boolean loginSuccessful(){
        return true;
    }
}