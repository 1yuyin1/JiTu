package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.luck.picture.lib.basic.PictureSelector;
import com.luck.picture.lib.config.SelectMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.interfaces.OnResultCallbackListener;


import java.io.File;
import java.util.ArrayList;

import cn.bmob.v3.Bmob;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

public class AlterActivity extends AppCompatActivity {
    User user;
    private String imagePath =null;
    private ImageView avatar_image;
    private Button change_avatar_button;
    private Button submit_button;
    private EditText new_uname_edit;
    private EditText new_signature_edit;
    private EditText old_password_edit;
    private EditText confirm_old_password_edit;
    private EditText new_password_edit;
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
        new_password_edit = findViewById(R.id.new_password_edit);
        Bmob.initialize(this, "f8a586e2cddf826ad840f9f072728ef6");

        if(user.getAvatar() != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(user.getAvatar().getLocalFile().getAbsolutePath());
            avatar_image.setImageBitmap(bitmap);
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
                String new_password = new_password_edit.getText().toString().trim();
                if("".equals(old_password) || "".equals(confirm_old_password)) {
                    Toast.makeText(AlterActivity.this,"请输入旧密码",Toast.LENGTH_SHORT).show();
                } else if (!(old_password.equals(confirm_old_password) )) {
                    Toast.makeText(AlterActivity.this,"两次旧密码不一致",Toast.LENGTH_SHORT).show();
                } else if(old_password.equals(confirm_old_password)) {
                    if(imagePath != null){
                        //更新头像
                        user.setAvatar(new BmobFile(new File(imagePath)));
                    }
                    if(!("".equals(new_uname))) {
                        user.setUsername(new_uname);
                    }
                    if(!("".equals(new_signature))) {
                        user.setSignature(new_signature);
                    }
                    if(!("".equals(new_password))) {
                        user.setPassword(new_password);
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
                    }
                    @Override
                    public void onCancel() {
                    }
                });
    }
    // 更新至 Bmob 服务器
    private void updateUser() {
        user.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e == null){
                    Intent intent = getIntent();
                    intent = new Intent(AlterActivity.this, MainActivity.class);
                    intent.putExtra("user",user);
                    startActivity(intent);
                    finishAffinity(); // 清除当前活动栈，避免返回到登录界面
                }else {
                }
            }
        });
    }
}