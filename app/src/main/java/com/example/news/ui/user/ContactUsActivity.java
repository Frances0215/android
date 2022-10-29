package com.example.news.ui.user;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import com.example.news.R;
import com.example.news.Utility;

public class ContactUsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        //改变通知栏的颜色（不去标题栏时用）
        Utility uni = new Utility();
        uni.setActionBar(this);


    }

    public void back_to_mine(View view) {
        finish();
    }



}
