package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class RegisterActivity extends AppCompatActivity {
    private Button ReButton;
    private EditText username;
    private EditText password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ReButton = (Button) findViewById(R.id.btn_register);
        username = (EditText) findViewById(R.id.et_username);
        password = (EditText) findViewById(R.id.et_password);
        Bmob.initialize(this,"f8a586e2cddf826ad840f9f072728ef6");


        ReButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username2 = username.getText().toString().trim();
                String password2 = password.getText().toString().trim();
                if("".equals(username2) || "".equals(password2)){
                    Toast.makeText(RegisterActivity.this, "用户名和密码都不得为空", Toast.LENGTH_LONG).show();
                }else if(username2.length() < 4 ||password2.length() < 6) {
                    Toast.makeText(RegisterActivity.this, "用户名不得少于4位，密码不得少于6位", Toast.LENGTH_LONG).show();
                } else {
                //将账号信息加入到数据库
                User user = new User();
                user.setUsername(username2);
                user.setPassword(password2);
                user.signUp(new SaveListener<Object>() {
                    @Override
                    public void done(Object o, BmobException e) {
                        if (e == null) {
                            Toast.makeText(RegisterActivity.this,"注册成功", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            intent.putExtra("user",user);
                            startActivity(intent);
                            finishAffinity(); // 清除当前活动栈，避免返回到登录界面
                        } else {
                            Toast.makeText(RegisterActivity.this,"注册失败"+e,Toast.LENGTH_LONG).show();
                        }
                    }
                });
                }
            }
        });
    }
}