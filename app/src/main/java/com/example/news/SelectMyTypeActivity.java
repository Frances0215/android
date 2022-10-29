package com.example.news;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class SelectMyTypeActivity extends AppCompatActivity {
    private Button btn[] = new Button[14];
    private Button mBtRegister;
    private Button mBtCancel;
    private boolean isCheck[] = new boolean[14];
    private ArrayList<String> myType = new ArrayList<>();

    private UserDataManager mUserDataManager;         //用户数据管理类
    private SharedPreferences login_sp;
    private TypeManager myTypeManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_my_type);
        //改变通知栏的颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }

        init();

        //初始化数据库
        if(myTypeManager == null){
            myTypeManager = new TypeManager(this);
            myTypeManager.openDataBase();
        }

        for(int i=0;i< btn.length;i++){
            btn[i].setTag(i);
            btn[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int a = (Integer)v.getTag();
                    if(!isCheck[a]){//之前没有选中
                        isCheck[a] = true;
                        btn[a].setSelected(isCheck[a]);
                        //改变全局变量中的值
                        if(!myType.contains(btn[a].getText()))
                            myType.add((String) btn[a].getText());

                    }else {//之前已经选中，再点击设为没选中状态
                        isCheck[a] = false;
                        btn[a].setSelected(isCheck[a]);
                        //删掉全局变量中的值
                        if(myType.contains(btn[a].getText()))
                            myType.remove((String) btn[a].getText());
                    }
                }
            });
        }

        if (mUserDataManager == null) {
            mUserDataManager = new UserDataManager(this);
            //mUserDataManager.openDataBase();                              //建立本地数据库
        }

        mBtRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //至少选择一个感兴趣的话题
                if(myType.size()<1){
                    Toast.makeText(SelectMyTypeActivity.this,"请至少选择一个频道",Toast.LENGTH_SHORT).show();
                }else {
                    //先将我的频道写入全局变量
                    NewsAPP mApp = (NewsAPP) getApplication();
                    mApp.setMy_news_type(myType);

//                    for(int i=0;i<myType.size();i++){
//                        myTypeManager.insertType(myType.get(i));
//                    }
                    //然后将用户信息写入数据库中
                    UserData mUser = mApp.getMyUser();
                    mUserDataManager.insertUserData(mUser);

                    //将用户信息写入手机缓存中
                    login_sp = getSharedPreferences("userInfo", 0);
                    SharedPreferences.Editor editor =login_sp.edit();
                    editor.putString("USER_ID", mUser.getID());
                    editor.putString("PASSWORD", mUser.getPassword());
                    editor.putBoolean("mRememberCheck", true);
                    editor.commit();

                    //跳转页面至主页面
                    Intent intent = new Intent(SelectMyTypeActivity.this, MainActivity.class);    //切换Login Activity至User Activity
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
                Intent intent = new Intent(SelectMyTypeActivity.this,LoginActivity.class) ;    //切换Login Activity至User Activity
                startActivity(intent);
                finish();
            }
        });

        //用于添加上方标题栏中的返回按钮
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public void init(){
        btn[0] = (Button) findViewById(R.id.mBt1);
        btn[1] = (Button)findViewById(R.id.mBt2);
        btn[2] = (Button)findViewById(R.id.mBt3);
        btn[3] = (Button)findViewById(R.id.mBt4);
        btn[4] = (Button)findViewById(R.id.mBt5);
        btn[5] = (Button)findViewById(R.id.mBt6);
        btn[6] = (Button)findViewById(R.id.mBt7);
        btn[7] = (Button)findViewById(R.id.mBt8);
        btn[8] = (Button)findViewById(R.id.mBt9);
        btn[9] = (Button)findViewById(R.id.mBt10);
        btn[10] = (Button)findViewById(R.id.mBt11);
        btn[11] = (Button)findViewById(R.id.mBt12);
        btn[12] = (Button)findViewById(R.id.mBt13);
        btn[13] = (Button)findViewById(R.id.mBt14);

        for(int n = 0;n<isCheck.length;n++){
            isCheck[n] = false;
        }

        mBtRegister = (Button)findViewById(R.id.mBtEnroll);
        mBtCancel = (Button) findViewById(R.id.mBtCancel);
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

    @Override
    protected void onResume() {
        if (mUserDataManager == null) {
            mUserDataManager = new UserDataManager(this);
            //mUserDataManager.openDataBase();
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
