package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class LoginActivity extends AppCompatActivity {
    private Button mButton;
    private EditText username;
    private EditText pass;
    private TextView register;
    private User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mButton = findViewById(R.id.loginButton);
        username = findViewById(R.id.usernameEditText);
        pass = findViewById(R.id.passwordEditText);
        register = findViewById(R.id.registerTextView);


        Bmob.initialize(this, "f8a586e2cddf826ad840f9f072728ef6");

        if (user.isLogin()) {
            //因为注册后的状态自动为已登录，所以获取注册活动的user，然后传递给mainActivity，避免发生空指针错误
            Intent intent = getIntent();
            user = (User) intent.getSerializableExtra("user");
            intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.putExtra("user",user);
            startActivity(intent);
            finishAffinity(); // 清除当前活动栈，避免返回到登录界面
        } else {
            mButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String username2 = username.getText().toString().trim();
                    String pass2 = pass.getText().toString().trim();
                    // 进行登录验证
                    if ("".equals(username2) || "".equals(pass2)) {
                        Toast.makeText(LoginActivity.this, "用户名和密码都不得为空", Toast.LENGTH_LONG).show();
                    } else if (username2.length() < 4 || pass2.length() < 6) {
                        Toast.makeText(LoginActivity.this, "用户名不得少于4位，密码不得少于6位", Toast.LENGTH_LONG).show();
                    } else {
                        user = new User();
                        user.setUsername(username2);
                        user.setPassword(pass2);
                        user.login(new SaveListener<Object>() {
                            @Override
                            public void done(Object o, BmobException e) {
                                if (e == null) {
                                    Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    intent.putExtra("user",user);
                                    startActivity(intent);
                                    finishAffinity(); // 清除当前活动栈，避免返回到登录界面
                                } else {
                                    Toast.makeText(LoginActivity.this, "登陆失败" + e, Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                }

                ;
            });
            register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                    finishAffinity();
                }
            });
        }
    }
}