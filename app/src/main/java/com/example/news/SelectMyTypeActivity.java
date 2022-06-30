package com.example.news;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_sex);
        init();

        for(int i=0;i< btn.length;i++){
            btn[i].setOnClickListener(new DBListener(i));
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

                    //然后将用户信息写入数据库中
                    UserData mUser = mApp.getMyUser();
                    mUserDataManager.insertUserData(mUser);

                    //将用户信息写入手机缓存中
                    login_sp = getSharedPreferences("userInfo", 0);
                    SharedPreferences.Editor editor =login_sp.edit();
                    editor.putString("USER_NAME", mUser.getName());
                    editor.putString("PASSWORD", mUser.getPassword());
                    editor.putString("USER_PHONE",mUser.getPhone());
                }
            }
        });
    }

    public void init(){
        btn[0] = findViewById(R.id.mBt1);
        btn[1] = findViewById(R.id.mBt2);
        btn[2] = findViewById(R.id.mBt3);
        btn[3] = findViewById(R.id.mBt4);
        btn[4] = findViewById(R.id.mBt5);
        btn[5] = findViewById(R.id.mBt6);
        btn[6] = findViewById(R.id.mBt7);
        btn[7] = findViewById(R.id.mBt8);
        btn[8] = findViewById(R.id.mBt9);
        btn[9] = findViewById(R.id.mBt10);
        btn[10] = findViewById(R.id.mBt11);
        btn[11] = findViewById(R.id.mBt12);
        btn[12] = findViewById(R.id.mBt13);
        btn[13] = findViewById(R.id.mBt14);

        for(int n = 0;n<isCheck.length;n++){
            isCheck[n] = false;
        }

        mBtRegister = (Button)findViewById(R.id.mBtEnroll);
        mBtCancel = (Button) findViewById(R.id.mBtCancel);
    }

    class DBListener implements View.OnClickListener {
        private int position;
        public DBListener(int p){
            this.position = p;
        }
        public void onClick(View arg0) {
            if(!isCheck[position]){//之前没有选中
                isCheck[position] = true;
                btn[position].setSelected(isCheck[position]);
                //改变全局变量中的值
                if(!myType.contains(btn[position].getText()))
                    myType.add((String) btn[position].getText());

            }else {//之前已经选中，再点击设为没选中状态
                isCheck[position] = false;
                btn[position].setSelected(isCheck[position]);
                //删掉全局变量中的值
                if(myType.contains(btn[position].getText()))
                    myType.remove((String) btn[position].getText());
            }
        }

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
