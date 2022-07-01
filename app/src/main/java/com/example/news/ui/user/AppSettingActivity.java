package com.example.news.ui.user;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.news.LoginActivity;
import com.example.news.NewsAPP;
import com.example.news.UserDataManager;
import com.example.news.R;

public class AppSettingActivity extends BaseActivity {

    private TextView fondSize;
    private TextView permission;
    private TextView usernameUpdate;
    private TextView passwordUpdate;
    private TextView accountDelete;
    private TextView exit;
    private UserDataManager mUserDataManager;
    private SharedPreferences mSharedPreferences;
//    private Switch sw_night_mode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_setting);

        if (mUserDataManager == null) {
            mUserDataManager = new UserDataManager(this);
            //mUserDataManager.openDataBase();                              //建立本地数据库
        }

        initView();
        setListeners();
    }

    private void initView(){
        fondSize = findViewById(R.id.tv_theme_setting);
        permission = findViewById(R.id.tv_app_permission);
        usernameUpdate = findViewById(R.id.tv_username_update);
        passwordUpdate = findViewById(R.id.tv_password_update);
        accountDelete = findViewById(R.id.tv_delete_account);
        exit = findViewById(R.id.tv_exit);
    }

    private void setListeners() {
        OnClick onClick = new OnClick();
        fondSize.setOnClickListener(onClick);
        permission.setOnClickListener(onClick);
        usernameUpdate.setOnClickListener(onClick);
        passwordUpdate.setOnClickListener(onClick);
        accountDelete.setOnClickListener(onClick);
        exit.setOnClickListener(onClick);
    }

    public void back_to_mine(View view) {
        finish();
    }

    //重写一个类
    private class OnClick implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.tv_theme_setting:
                    Intent intent = new Intent(AppSettingActivity.this, ThemeActivity.class);
                    startActivity(intent);
                    break;
                case R.id.tv_app_permission:
                    Intent localIntent = new Intent();
                    localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    if (Build.VERSION.SDK_INT >= 9) {
                        localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                        localIntent.setData(Uri.fromParts("package", getPackageName(), null));
                    } else if (Build.VERSION.SDK_INT <= 8) {
                        localIntent.setAction(Intent.ACTION_VIEW);
                        localIntent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
                        localIntent.putExtra("com.android.settings.ApplicationPkgName", getPackageName());
                    }
                    startActivity(localIntent);
                    break;

                case R.id.tv_username_update:
                    AlertDialog.Builder builder2 = new AlertDialog.Builder(AppSettingActivity.this);
                    View v1 = LayoutInflater.from(AppSettingActivity.this).inflate(R.layout.edit_dialog, null);
                    EditText etUsername = v1.findViewById(R.id.et_text);
                    builder2.setTitle("修改用户名").setView(v1).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @RequiresApi(api = Build.VERSION_CODES.P)
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(AppSettingActivity.this, "修改成功", Toast.LENGTH_SHORT);
//                          //修改数据库中的用户名数据
                            String userName = etUsername.getText().toString();
                            NewsAPP mApp = (NewsAPP)getApplication();
                            String id = mApp.getMyUser().getID();
                            mUserDataManager.updateUserDataById(userName,"name",id);
                            Toast.makeText(AppSettingActivity.this, "用户名修改成功",Toast.LENGTH_SHORT).show();

                        }
                    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //取消修改
                        }
                    }).show();
                    break;

                case R.id.tv_password_update:
                    AlertDialog.Builder builder3 = new AlertDialog.Builder(AppSettingActivity.this);
                    View v2 = LayoutInflater.from(AppSettingActivity.this).inflate(R.layout.edit_dialog, null);
                    EditText etWorkplace = v2.findViewById(R.id.et_text);
                    builder3.setTitle("修改登录密码").setView(v2).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(AppSettingActivity.this, "修改成功", Toast.LENGTH_SHORT);
//                          //修改数据库中的密码数据
                        }
                    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //取消修改
                        }
                    }).show();
                    break;

                case R.id.tv_delete_account:
                    AlertDialog.Builder builder_delete = new AlertDialog.Builder(AppSettingActivity.this);
                    builder_delete
                            .setMessage("是否注销账号？")
                            .setPositiveButton("是", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    NewsAPP mApp = (NewsAPP)getApplication();
                                    String id = mApp.getID();
                                    mUserDataManager.deleteUserData(id);
                                    mApp.clearUser();
                                    Intent intent = new Intent(AppSettingActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                    //清除数据库中的账户数据
                                }
                            }).setNegativeButton("否", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    //取消
                                }
                            }).show();
                    break;

                case R.id.tv_exit:
                    AlertDialog.Builder builder_exit = new AlertDialog.Builder(AppSettingActivity.this);
                    builder_exit
                            .setMessage("是否退出登录？")
                            .setPositiveButton("是", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(AppSettingActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                }
                            }).setNegativeButton("否", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    //取消修改
                                }
                            }).show();

                    break;
            }
        }

    }

    @Override
    protected void onPause() {
        if (mUserDataManager != null) {
            //mUserDataManager.closeDataBase();
            mUserDataManager = null;
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        if (mUserDataManager == null) {
            mUserDataManager = new UserDataManager(this);
            //mUserDataManager.openDataBase();
        }
        super.onResume();
    }

}