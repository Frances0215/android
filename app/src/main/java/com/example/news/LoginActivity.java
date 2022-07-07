package com.example.news;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.os.Message;
import android.preference.PreferenceManager;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

public class LoginActivity extends Activity {
    public int pwdResetFlag=0;

    private EditText mEtPassword;
    private EditText mEtAccount;
    private CheckBox mCbRememberPass;
    private TextView mTvRememberPassword;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private TextView mTvEnroll;
    private TextView mTvReset;
    private ImageView mIvEye;
    //private Boolean isValid[] = new Boolean[5];
    //private FaceIdentify mFace;
    private boolean isOpenEye = false;
    private SharedPreferences login_sp;
    private String userNameValue,passwordValue;

    private UserDataManager mUserDataManager;         //用户数据管理类


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //通过id找到相应的控件

        Button mBtLogin=(Button)findViewById(R.id.button_login);
        mEtPassword=(EditText)findViewById(R.id.edit_password);
        mEtAccount=(EditText)findViewById(R.id.edit_account);
        pref= PreferenceManager.getDefaultSharedPreferences(this);
        mCbRememberPass=(CheckBox)findViewById(R.id.checkBox_password);
        mTvEnroll = (TextView)findViewById(R.id.mTvEnroll);
        mTvReset = (TextView)findViewById(R.id.mTvReset);
        mIvEye = (ImageView)findViewById(R.id.mIvEye);
        //mFace = new FaceIdentify();


        login_sp = getSharedPreferences("userInfo", 0);
        String id=login_sp.getString("USER_ID", "");
        String pwd =login_sp.getString("PASSWORD", "");
        boolean choseRemember =login_sp.getBoolean("mRememberCheck", false);
        boolean choseAutoLogin =login_sp.getBoolean("mAutologinCheck", false);
        //如果上次选了记住密码，那进入登录页面也自动勾选记住密码，并填上用户名和密码
        if(choseRemember){
            mEtAccount.setText(id);
            mEtPassword.setText(pwd);
            mCbRememberPass.setChecked(true);
        }

        if (mUserDataManager == null) {
            mUserDataManager = new UserDataManager(this);
            //mUserDataManager.openDataBase();                              //建立本地数据库
        }

        mTvEnroll.setOnClickListener(new View.OnClickListener(){

            @RequiresApi(api = Build.VERSION_CODES.P)
            @Override
            public void onClick(View v) {
//                NewsAPP mUser = (NewsAPP)getApplicationContext();
//                mUser.setmUserId(mUserDataManager.findUserIdByName(name));
                Log.e("这里","1");
                Intent intent_Login_to_Register = new Intent(LoginActivity.this,EnrollActivity.class) ;    //切换Login Activity至User Activity
                startActivity(intent_Login_to_Register);
                finish();
            }
        });

        mBtLogin.setOnClickListener(new View.OnClickListener(){
            @RequiresApi(api = Build.VERSION_CODES.P)
            @Override
            public void onClick(View v) {
                login();
            }
        });

        mTvReset.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent_Login_to_Reset = new Intent(LoginActivity.this,ResetPasswordActivity.class) ;    //切换Login Activity至User Activity
                startActivity(intent_Login_to_Reset);
                finish();
            }
        });

        mIvEye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isOpenEye) {
                    mIvEye.setSelected(true);
                    isOpenEye = true;
                    //密码可见
                    mEtPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }else{
                    mIvEye.setSelected(false);
                    isOpenEye = false;
                    //密码不可见
                    mEtPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

    }

//    public void announce(){
//        View.announceForAccessibility("正在刷新");
//    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    public void login() {
        //登录按钮监听事件
        if (isUserNameAndPwdValid()) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String userId = mEtAccount.getText().toString().trim();    //获取当前输入的用户名和密码信息
                    String userPwd = mEtPassword.getText().toString().trim();
                    SharedPreferences.Editor editor =login_sp.edit();
                    boolean flag = mUserDataManager.isUserValid(userId, userPwd);
                    Message message = new Message();
                    if(flag){
                        //保存用户名和密码
                        editor.putString("USER_ID", userId);
                        editor.putString("PASSWORD", userPwd);
                        //是否记住密码
                        if(mCbRememberPass.isChecked()){
                            editor.putBoolean("mRememberCheck", true);
                        }else{
                            editor.putBoolean("mRememberCheck", false);
                        }
                        editor.commit();
                        //记录登录的ID
                        NewsAPP app =(NewsAPP) getApplication();
                        app.setUserID(userId);
                        Intent intent = new Intent(LoginActivity.this,MainActivity.class) ;    //切换Login Activity至User Activity
                        startActivity(intent);
                        finish();

                        Looper.prepare();
                        Toast.makeText(LoginActivity.this, "登录成功",Toast.LENGTH_SHORT).show();//登录成功提示
                        Looper.loop();
                    }else
                        message.what = 1;
                        Looper.prepare();
                        Toast.makeText(LoginActivity.this, "账号或密码错误",Toast.LENGTH_SHORT).show();
                        Looper.loop();
                }

            }).start();

        }
    }

    public boolean isUserNameAndPwdValid() {
        if (mEtAccount.getText().toString().trim().equals("")) {
            Toast.makeText(this, "账号不能为空",
                    Toast.LENGTH_SHORT).show();
            return false;
        } else if (mEtPassword.getText().toString().trim().equals("")) {
            Toast.makeText(this, "密码不能为空",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    @Override
    protected void onResume() {
        if (mUserDataManager == null) {
            mUserDataManager = new UserDataManager(this);
            //.openDataBase();
        }
        super.onResume();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    @Override
    protected void onPause() {
        if (mUserDataManager != null) {
            //mUserDataManager.closeDataBase();
            mUserDataManager = null;
        }
        super.onPause();
    }
}
