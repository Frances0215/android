package com.example.news;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences login_sp;
    //在手机缓存中只存入用户名、密码、手机号码以及是否记住密码
    private UserDataManager mUserDataManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //去掉标题栏
        this.getSupportActionBar().hide();//注意是在 setContentView(R.layout.activity_main)后

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_photograph, R.id.navigation_user)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        if (mUserDataManager == null) {
            mUserDataManager = new UserDataManager(this);
            mUserDataManager.openDataBase();                              //建立本地数据库
        }
        //查看preference中是否存有用户号和密码
        login_sp = getSharedPreferences("userInfo", 0);
        String name=login_sp.getString("USER_NAME", "");
        String pwd =login_sp.getString("PASSWORD", "");
        String phone = login_sp.getString("PHONE","");
        boolean choseRemember =login_sp.getBoolean("mRememberCheck", false);
        boolean choseAutoLogin =login_sp.getBoolean("mAutologinCheck", false);
        boolean isLogin = mUserDataManager.isUserValid(phone,pwd);
        if(!isLogin){
            Intent intent_Login = new Intent(MainActivity.this,LoginActivity.class) ;    //切换Login Activity至User Activity
            startActivity(intent_Login);
            finish();
        }else {
            NewsAPP mNews = (NewsAPP)getApplicationContext();
            mNews.setPwd(pwd);
            mNews.setUserPhone(phone);
        }

    }


    @Override
    protected void onRestart() {
        super.onRestart();
    }


}