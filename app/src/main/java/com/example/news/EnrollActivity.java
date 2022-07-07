package com.example.news;

import android.app.Activity;
import android.content.Intent;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class EnrollActivity extends AppCompatActivity {
//    private EditText mAccount;                        //用户名编辑
//    private EditText mPwd;                            //密码编辑
//    private EditText mPwdCheck;                       //密码编辑
//    private Button mSureButton;                       //确定按钮

    private UserDataManager mUserDataManager;         //用户数据管理类

    private Button mBtEnroll;
    private EditText mEtAccount;
    private EditText mEtPass;
    private EditText mEtSure;
    private TextView mBtCancel;                     //取消按钮
    private ImageView mIvEye;
    private ImageView mIvReEye;
    private EditText mEtName;
    private boolean isOpenEye = false;
    private boolean isOpenEye2 = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enroll);
        mBtEnroll = (Button)findViewById(R.id.mBtEnroll);
        mEtName = (EditText)findViewById(R.id.mEtName);
        mEtPass = (EditText)findViewById(R.id.mEtPassword);
        mEtName = (EditText)findViewById(R.id.mEtName);
        mEtAccount=(EditText)findViewById(R.id.mEtAccount);
        mEtSure = (EditText)findViewById(R.id.mEtSure);
        mBtCancel = (TextView)findViewById(R.id.mBtCancel);
        mIvEye = (ImageView)findViewById(R.id.mIvEye);
        mIvReEye = (ImageView)findViewById(R.id.mIvReEye);
        if (mUserDataManager == null) {
            mUserDataManager = new UserDataManager(this);
            //mUserDataManager.openDataBase();                              //建立本地数据库
        }

        mBtCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_Register_to_Login = new Intent(EnrollActivity.this,LoginActivity.class) ;    //切换User Activity至Login Activity
                startActivity(intent_Register_to_Login);
                finish();
            }
        });

        //点击注册按钮
        mBtEnroll.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.P)
            @Override
            public void onClick(View v) {
                register_check();
            }
        });

        //点击密码是否可见
        mIvEye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isOpenEye2) {
                    mIvEye.setSelected(true);
                    isOpenEye2 = true;
                    //密码可见
                    mEtPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }else{
                    mIvEye.setSelected(false);
                    isOpenEye2 = false;
                    //密码不可见
                    mEtPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        mIvReEye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isOpenEye) {
                    mIvReEye.setSelected(true);
                    isOpenEye = true;
                    //密码可见
                    mEtSure.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }else{
                    mIvReEye.setSelected(false);
                    isOpenEye = false;
                    //密码不可见
                    mEtSure.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        //用于添加上方标题栏中的返回按钮
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    public void register_check() {                                //确认按钮的监听事件
        if (isUserNameAndPwdValid()) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String userAccount = mEtAccount.getText().toString().trim();
                    String userName = mEtName.getText().toString().trim();
                    String userPwd = mEtPass.getText().toString().trim();
                    String userPwdCheck = mEtSure.getText().toString().trim();
                    //检查用户是否存在
                    boolean isValid=mUserDataManager.findUserByPhone(userAccount);
                    //用户已经存在时返回，给出提示文字
                    if(isValid){
                        Looper.prepare();
                        Toast.makeText(EnrollActivity.this, "该用户已存在",Toast.LENGTH_SHORT).show();
                        Looper.loop();
                        return ;
                    }
                    if(userPwd.equals(userPwdCheck)==false){     //两次密码输入不一样
                        Looper.prepare();
                        Toast.makeText(EnrollActivity.this, "两次密码输入不相同",Toast.LENGTH_SHORT).show();
                        Looper.loop();
                        return ;
                    } else {//注册用户
                        //不直接注册用户，先存入全局变量中，最后再存入数据库中

                        NewsAPP mApp = (NewsAPP) getApplication();
                        mApp.setUserID(userAccount);
                        mApp.setPwd(userPwd);
                        mApp.setName(userName);
                        mApp.getMyUser().setSex("女");
                        mApp.getMyUser().setBirthday("2000-02-07");
                        SharedPreferences login_sp;
                        login_sp = getSharedPreferences("userInfo", MODE_PRIVATE);
                        SharedPreferences.Editor editor =login_sp.edit();

                        //然后将用户信息写入数据库中
                        UserData mUser = mApp.getMyUser();
                        mUserDataManager.insertUserData(mUser);

                        //将用户信息写入手机缓存中
                        editor.putString("USER_ID", userAccount);
                        editor.putString("PASSWORD", userPwd);
                        editor.putBoolean("mRememberCheck", true);
                        editor.putBoolean("mAutologinCheck", false);
                        editor.commit();


                        //跳转到性别选择页面
                        Intent intent_Register_to_Login = new Intent(EnrollActivity.this,MainActivity.class) ;    //切换User Activity至Login Activity
                        startActivity(intent_Register_to_Login);
                        finish();

                    }
                }



            }).start();
        }
    }

    //查看所有信息是否已经填好
    public boolean isUserNameAndPwdValid() {
        if (mEtName.getText().toString().trim().equals("")) {
            Toast.makeText(this, "用户名不能为空",
                    Toast.LENGTH_SHORT).show();
            return false;
        } else if (mEtAccount.getText().toString().trim().equals("")) {
            Toast.makeText(this, "账号不能为空",
                    Toast.LENGTH_SHORT).show();
            return false;
        }else if (mEtPass.getText().toString().trim().equals("")) {
            Toast.makeText(this, "密码不能为空",
                    Toast.LENGTH_SHORT).show();
            return false;
        }else if(mEtSure.getText().toString().trim().equals("")) {
            Toast.makeText(this,"请确认您的密码",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    //返回上一个界面
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish(); // back button
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}