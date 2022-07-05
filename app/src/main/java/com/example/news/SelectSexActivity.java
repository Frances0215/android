package com.example.news;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.news.ui.home.News;

public class SelectSexActivity extends AppCompatActivity {
    private FontIconView mIvBoy;
    private FontIconView mIvGirl;
    private Button mBtNext;
    private Button mBtCancel;
    private boolean isBoy = false;
    private boolean isGirl = false;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_sex);
        init();

        mIvBoy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isBoy) {//开始时不是选择boy，则换成Boy,之后再点没有用
                    mIvBoy.setSelected(true);
                    isBoy = true;
                    isGirl = false;
                    mIvGirl.setSelected(false);
                }
            }
        });

        mIvGirl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isGirl) {
                    mIvGirl.setSelected(true);
                    isGirl = true;
                    isBoy = false;
                    mIvBoy.setSelected(isBoy);
                }
            }
        });

        mBtNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isGirl || isBoy){//只要有一个就行
                    String sex;
                    if(isGirl)
                        sex="女";
                    else
                        sex="男";

                    //更改全局变量中的值
                    NewsAPP mApp = (NewsAPP) getApplication();
                    mApp.getMyUser().setSex(sex);

                    //跳转页面至选择生日
                    Intent intent = new Intent(SelectSexActivity.this,SelectBirthdayActivity.class) ;    //切换Login Activity至User Activity
                    startActivity(intent);
                    finish();

                }
            }
        });

        mBtCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //先把全局变量中的值清空
                NewsAPP mApp = (NewsAPP) getApplication();
                mApp.clearUser();

                //跳转页面至登录页面
                Intent intent = new Intent(SelectSexActivity.this,LoginActivity.class) ;    //切换Login Activity至User Activity
                startActivity(intent);
                finish();
            }
        });
    }

    public void init(){
        mIvBoy = findViewById(R.id.mIvBoy);
        mIvGirl = findViewById(R.id.mIvGirl);
        mBtNext = findViewById(R.id.mBtNext);
        mBtCancel = findViewById(R.id.mBtCancel);
    }
}
