package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.NetworkOnMainThreadException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {
    private final Gson gson = new Gson();

    private Button mButton;
    private EditText username;
    private EditText pass;
    private TextView register;
    String username2;
    String pass2;
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
                    username2 = username.getText().toString().trim();
                    pass2 = pass.getText().toString().trim();
                    // 进行登录验证
                    if ("".equals(username2) || "".equals(pass2)) {
                        Toast.makeText(LoginActivity.this, "用户名和密码都不得为空", Toast.LENGTH_LONG).show();
                    } else if (username2.length() < 4 || pass2.length() < 6) {
                        Toast.makeText(LoginActivity.this, "用户名不得少于4位，密码不得少于6位", Toast.LENGTH_LONG).show();
                    } else {
                        login(username2,pass2);
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

    private void login(String username,String password){
        new Thread(() -> {
            // url路径
            String url = "http://47.107.52.7:88/member/photo/user/login?password="+ password + "&username=" + username;
            // 请求头
            Headers headers = new Headers.Builder()
                    .add("Accept", "application/json, text/plain, */*")
                    .add("appId", "9434b7cbae6644f4bb2965c9fa46a20a")
                    .add("appSecret", "07005e8f8e038ed074a21a1d44eb2e6d9cc71")
                    .add("Content-Type", "application/json")
                    .build();

            MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");

            //请求组合创建
            Request request = new Request.Builder()
                    .url(url)
                    // 将请求头加至请求中
                    .headers(headers)
                    .post(RequestBody.create(MEDIA_TYPE_JSON, ""))
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
            ResponseBody<String> dataResponseBody = gson.fromJson(body,jsonType);
            Log.d("info", dataResponseBody.toString());

            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.putExtra("user",dataResponseBody.toString());
            startActivity(intent);
            finishAffinity(); // 清除当前活动栈，避免返回到登录界面
        }
    };

}