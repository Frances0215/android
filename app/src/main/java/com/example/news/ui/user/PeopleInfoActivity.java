package com.example.news.ui.user;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.AbstractWindowedCursor;
import android.database.Cursor;
import android.database.CursorWindow;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;
import android.graphics.drawable.BitmapDrawable;


import com.example.news.LoginActivity;
import com.example.news.NewsAPP;
import com.example.news.R;
import com.example.news.ResetPasswordActivity;
import com.example.news.UserData;
import com.example.news.UserDataManager;
import com.example.news.VoiceRecord;
import com.example.news.VoiceTrans;
import com.example.news.ui.photograph.ImageUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class PeopleInfoActivity extends BaseActivity {
    private static final int REQUEST_CODE_TAKE = 1;
    private static final int REQUEST_CODE_CHOOSE = 0;
    private ImageView mPicture;
    private TextView mName;
    private TextView mBirthday;
    private TextView mGender;
    private TextView mPhoneNumber;
    private TextView mType;
    private TextView mPassword;
    private Button mBtSave;
    private String birthday;
    private String birthdayTime;

    private Context mContext = null;

    private Uri imageUri; //定位资源位置
    private String imageBase64;

    private UserDataManager mUserDataManager;

    private VoiceTrans myTrans = new VoiceTrans();
    private VoiceRecord myVoice = new VoiceRecord();

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mUserDataManager == null) {
            mUserDataManager = new UserDataManager(this);
            //.openDataBase();                              //建立本地数据库
        }
        setContentView(R.layout.activity_people_information);
        initView();
        initData();
        setListeners();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(PeopleInfoActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1000);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, 2000);
        }

    }


    private void initView(){
        mName = findViewById(R.id.tv_username_text);
        mBirthday = findViewById(R.id.tv_birthday_text);
        mGender = findViewById(R.id.tv_gender_text);
        mPhoneNumber = findViewById(R.id.tv_phone_text);
        mType = findViewById(R.id.tv_type_text);
        mPassword = findViewById(R.id.tv_password_text);
        mBtSave = findViewById(R.id.mBtSave);
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    private void initData(){
        getDataFromSpf();
    }

    public void save(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String name = mName.getText().toString();
                String birthday = mBirthday.getText().toString();
                String type = mType.getText().toString();
                if(type.equals("")){
                    type=null;
                }
                String phone = mPhoneNumber.getText().toString();
                String gender = mGender.getText().toString();
                //存入数据库中
                //UserData userData = new UserData(name,gender,birthday,phone);
                int result = mUserDataManager.updateUserData(phone,birthday,gender,name,type);
                if(result>0){
                    Looper.prepare();
                    Toast.makeText(PeopleInfoActivity.this,"已保存修改。",Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }else {
                    Looper.prepare();
                    Toast.makeText(PeopleInfoActivity.this,"修改失败。",Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }

            }
        }).start();

//        this.finish(); //退出该页面
    }

    final Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    UserData user = (UserData) msg.obj;
                    String name = user.getName();
                    String birthday = user.getBirthday();
                    String phone = user.getID();
                    String gender = user.getSex();
                    String type = user.getType();

                    mName.setText(name);
                    mBirthday.setText(birthday);
                    mPhoneNumber.setText(phone);
                    mGender.setText(gender);
                    mType.setText(type);
                    break;
                case 1://语音输入的结果
                    mName.setText((String)msg.obj);
                default:
                    break;
            }
        }

    };
    @RequiresApi(api = Build.VERSION_CODES.P)
    private void getDataFromSpf(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                NewsAPP mApp = (NewsAPP)getApplication();
                String id = mApp.getID();//id与phone相同
                UserData mUser = mUserDataManager.fetchUserData(id);
                Message message = new Message();
                message.obj =mUser;
                message.what = 0;
                mHandler.sendMessage(message);
            }
        }).start();

    }

    private void setListeners() {
        OnClick onClick = new OnClick();
        mBirthday.setOnClickListener(onClick);
        mName.setOnClickListener(onClick);
        mGender.setOnClickListener(onClick);
        mPhoneNumber.setOnClickListener(onClick);
        mType.setOnClickListener(onClick);
        mPassword.setOnClickListener(onClick);
        mBtSave.setOnClickListener(onClick);
    }



    //重写一个类
    class OnClick implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {

                case R.id.tv_gender_text:
                    final String[] gender = new String[]{"男", "女"};
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(PeopleInfoActivity.this);
                    builder1.setTitle("选择性别").setSingleChoiceItems(gender, 0, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    mGender.setText(gender[i]);
                                    Toast.makeText(PeopleInfoActivity.this, "修改成功", Toast.LENGTH_SHORT);
                                    dialogInterface.dismiss();
                                }
                            }
                    ).show();
                    break;

                case R.id.tv_username_text:
                    AlertDialog.Builder builder2_1 = new AlertDialog.Builder(PeopleInfoActivity.this);
                    View v1_1 = LayoutInflater.from(PeopleInfoActivity.this).inflate(R.layout.edit_choose, null);
                    Button mBtVoice = v1_1.findViewById(R.id.mBtVoice);
                    Button mBtKey = v1_1.findViewById(R.id.mBtKey);
                    builder2_1.setTitle("选择输入方式").setView(v1_1);
                    AlertDialog dialog2 = builder2_1.show();

                    mBtVoice.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog2.dismiss();
                            myVoice.startRecord(myVoice.getFileName());
                            AlertDialog.Builder builder = new AlertDialog.Builder(PeopleInfoActivity.this);

                            View view2 = View.inflate(PeopleInfoActivity.this, R.layout.voice_input, null);
                            final LinearLayout mLlVoice = (LinearLayout) view2.findViewById(R.id.mLlVoice);
                            builder.setView(view2).setPositiveButton("结束语音输入", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    myVoice.stopRecord();
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                String result = myTrans.voiceTrans(myVoice.getFileName());
                                                //String result = "哈哈";
                                                Message message = new Message();
                                                message.obj =result;
                                                message.what = 1;
                                                mHandler.sendMessage(message);

                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }).start();
                                }
                            });

                            builder.create();
                            AlertDialog dialog = builder.show();
                            dialog.getWindow().setLayout(1000,800);

                            //设置确定按钮的位置大小
                            Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                            positiveButton.setTextSize(35);
                            positiveButton.setTextColor(Color.BLACK);
                            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) positiveButton.getLayoutParams();
                            layoutParams.weight = 10;
                            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setLayoutParams(layoutParams);
                            //设置使点击空白处不能关闭
                            dialog.setCanceledOnTouchOutside(false);
                            mLlVoice.setOnClickListener(new View.OnClickListener() {

                                @Override

                                public void onClick(View v) {
                                    myVoice.stopRecord();
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                String result = myTrans.voiceTrans(myVoice.getFileName());
                                                //String result = myTrans.voiceTrans(FILE_NAME);
                                                //String result="哈哈";
                                                Message message = new Message();
                                                message.obj =result;
                                                message.what = 2;
                                                mHandler.sendMessage(message);

                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }).start();
                                    dialog.dismiss();
                                }
                            });

                        }
                    });//语音输入借宿

                    mBtKey.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog2.dismiss();
                            AlertDialog.Builder builder2 = new AlertDialog.Builder(PeopleInfoActivity.this);
                            View v1 = LayoutInflater.from(PeopleInfoActivity.this).inflate(R.layout.edit_dialog, null);
                            EditText etUsername = v1.findViewById(R.id.et_text);
                            builder2.setTitle("修改昵称").setView(v1).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Toast.makeText(PeopleInfoActivity.this, "修改成功", Toast.LENGTH_SHORT);
                                    mName.setText(etUsername.getText());
                                }
                            }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    //取消修改
                                }
                            }).show();
                        }
                    });



                    break;

                case R.id.tv_birthday_text:
                    new DatePickerDialog(PeopleInfoActivity.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                            int realMonth = month +1; //month默认从0开始
                            birthday = year + "/" + realMonth + "/" + dayOfMonth;
                            mBirthday.setText(birthday);
                        }
                    },1989,01,01).show();
                    break;

                case R.id.tv_password_text:
                    //首先判断是否是自动登录的用户

                    //直接切换到修改密码页面，之后跳转到登录界面再登录一次
                    Intent intent_Login_to_Reset = new Intent(PeopleInfoActivity.this, ResetPasswordActivity.class) ;
                    startActivity(intent_Login_to_Reset);
                    finish();

                case R.id.tv_type_text:
                    final String[] userType = new String[]{"弱视", "全盲","正常"};
                    AlertDialog.Builder builder_type = new AlertDialog.Builder(PeopleInfoActivity.this);
                    builder_type.setTitle("选择用户类别").setSingleChoiceItems(userType, 0, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    mType.setText(userType[i]);
                                    Toast.makeText(PeopleInfoActivity.this, "修改成功", Toast.LENGTH_SHORT);
                                    dialogInterface.dismiss();
                                }
                            }
                    ).show();
                    break;

                case R.id.mBtSave:
                    save();
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