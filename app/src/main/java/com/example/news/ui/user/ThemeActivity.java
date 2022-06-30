package com.example.news.ui.user;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import com.example.news.R;
import com.example.news.ui.user.DataClass.StatcClass;
import com.example.news.ui.user.ToolClass.DataAccess;
import com.example.news.ui.user.ToolClass.L;

public class ThemeActivity extends BaseActivity implements View.OnClickListener {


    private Vibrator mVibrator; //创建震动服务对象

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme);

        //获取手机震动的服务
        mVibrator=(Vibrator)getApplication().getSystemService(Service.VIBRATOR_SERVICE);

        initView();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initView() {

        Button Size01 = findViewById(R.id.Size01);
        Button Size02 = findViewById(R.id.Size02);
        Button Size03 = findViewById(R.id.Size03);
        Button Size04 = findViewById(R.id.Size04);
        Button Size05 = findViewById(R.id.Size05);

        Button Font01 = findViewById(R.id.Font01);
        Button Font02 = findViewById(R.id.Font02);
        Button Font03 = findViewById(R.id.Font03);
        Button Font04 = findViewById(R.id.Font04);

        Button shake01 = findViewById(R.id.shake01);
        Button shake02 = findViewById(R.id.shake02);
        Button shake03 = findViewById(R.id.shake03);
        Button shake04 = findViewById(R.id.shake04);

        Size01.setOnClickListener(this);
        Size02.setOnClickListener(this);
        Size03.setOnClickListener(this);
        Size04.setOnClickListener(this);
        Size05.setOnClickListener(this);

        Font01.setOnClickListener(this);
        Font02.setOnClickListener(this);
        Font03.setOnClickListener(this);
        Font04.setOnClickListener(this);

        shake01.setOnClickListener(this);
        shake02.setOnClickListener(this);
        shake03.setOnClickListener(this);
        shake04.setOnClickListener(this);

        SeekBar seekbar02 = findViewById(R.id.seekBar01);
        //int position = readCodingInt("position");
        int position = DataAccess.ReadDataInt(ThemeActivity.this, "position");
        seekbar02.setMax(4);  //最大值为4
        seekbar02.setProgress(position);  //默认值为2
        seekbar02.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                L.e("拖动过程中的值："+ String.valueOf(progress) + ":" + String.valueOf(fromUser));
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                L.e("开始滑动时的值："+ String.valueOf(seekBar.getProgress()));
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                L.e("停止滑动时的值：" + String.valueOf(seekBar.getProgress()));

                switch (seekBar.getProgress()){
                    case 0:
                        StatcClass.Size = 0.5f;
                        break;
                    case 1:
                        StatcClass.Size = 0.7f;
                        break;
                    case 2:
                        StatcClass.Size = 0.9f;
                        break;
                    case 3:
                        StatcClass.Size = 1.1f;
                        break;
                    case 4:
                        StatcClass.Size = 1.3f;
                        break;
                }
                DataAccess.DeleteData(ThemeActivity.this,"size");
                DataAccess.SaveDataFloat(ThemeActivity.this,"size", StatcClass.Size);
                DataAccess.SaveDataInt(ThemeActivity.this,"position",seekBar.getProgress());  //储存进度条的位置

                ResetActivity();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.Size01:
                StatcClass.Size = 0.5f;
                DataAccess.DeleteData(ThemeActivity.this,"size");  //删除数据
                DataAccess.SaveDataFloat(ThemeActivity.this,"size", StatcClass.Size);
                DataAccess.SaveDataInt(ThemeActivity.this,"position",0);  //储存进度条的位置
                ResetActivity();
                break;
            case R.id.Size02:
                StatcClass.Size = 0.7f;
                DataAccess.DeleteData(ThemeActivity.this,"size");  //删除数据
                DataAccess.SaveDataFloat(ThemeActivity.this,"size", StatcClass.Size);
                DataAccess.SaveDataInt(ThemeActivity.this,"position",1);  //储存进度条的位置
                ResetActivity();
                break;
            case R.id.Size03:
                StatcClass.Size = 0.9f;
                DataAccess.DeleteData(ThemeActivity.this,"size");  //删除数据
                DataAccess.SaveDataFloat(ThemeActivity.this,"size", StatcClass.Size);
                DataAccess.SaveDataInt(ThemeActivity.this,"position",2);  //储存进度条的位置
                ResetActivity();
                break;
            case R.id.Size04:
                StatcClass.Size = 1.1f;
                DataAccess.DeleteData(ThemeActivity.this,"size");  //删除数据
                DataAccess.SaveDataFloat(ThemeActivity.this,"size", StatcClass.Size);
                DataAccess.SaveDataInt(ThemeActivity.this,"position",3);  //储存进度条的位置
                ResetActivity();
                break;
            case R.id.Size05:
                StatcClass.Size = 1.3f;
                DataAccess.DeleteData(ThemeActivity.this,"size");  //删除数据
                DataAccess.SaveDataFloat(ThemeActivity.this,"size", StatcClass.Size);
                DataAccess.SaveDataInt(ThemeActivity.this,"position",4);  //储存进度条的位置
                ResetActivity();
                break;


            case R.id.Font01:
                StatcClass.FONTS = 1;
                DataAccess.SaveDataInt(ThemeActivity.this,"FONTS", StatcClass.FONTS);
                ResetActivity();
                break;
            case R.id.Font02:
                StatcClass.FONTS = 2;
                DataAccess.SaveDataInt(ThemeActivity.this,"FONTS", StatcClass.FONTS);
                ResetActivity();
                break;
            case R.id.Font03:
                StatcClass.FONTS = 3;
                DataAccess.SaveDataInt(ThemeActivity.this,"FONTS", StatcClass.FONTS);
                ResetActivity();
                break;
            case R.id.Font04:
                StatcClass.FONTS = 4;
                DataAccess.SaveDataInt(ThemeActivity.this,"FONTS", StatcClass.FONTS);
                ResetActivity();
                break;

//            case R.id.Theme01:
//                StatcClass.THEME = 1;
//                DataAccess.SaveDataInt(MainActivity.this,"THEME", StatcClass.THEME);
//                ResetActivity();
//                break;
//            case R.id.Theme02:
//                StatcClass.THEME = 2;
//                DataAccess.SaveDataInt(MainActivity.this,"THEME", StatcClass.THEME);
//                ResetActivity();
//                break;
//            case R.id.Theme03:
//                StatcClass.THEME = 3;
//                DataAccess.SaveDataInt(MainActivity.this,"THEME", StatcClass.THEME);
//                ResetActivity();
//                break;
//            case R.id.Theme04:
//                StatcClass.THEME = 4;
//                DataAccess.SaveDataInt(MainActivity.this,"THEME", StatcClass.THEME);
//                ResetActivity();
//                break;

            case R.id.shake01:
                mVibrator.vibrate(new long[]{100,100},-1);
                break;
            case R.id.shake02:
                mVibrator.vibrate(new long[]{100,100,100,1000},0);
                break;
            case R.id.shake03:
                mVibrator.vibrate(new long[]{1,1000},0);
                break;
            case R.id.shake04:
                mVibrator.cancel();
                break;

        }
    }


//    // 本地语言设置 关键代码
//    private void set(String language_type) {
//        Locale myLocale = new Locale(language_type);
//        Resources res = getResources();
//        DisplayMetrics dm = res.getDisplayMetrics();
//        Configuration conf = res.getConfiguration();
//        conf.locale = myLocale;
//        res.updateConfiguration(conf,dm);
//    }

    //重启MainActivity，用于设置成功后实时刷新页面
    private void ResetActivity(){
        Intent intent=new Intent( ThemeActivity.this, ThemeActivity.class);  //跳转到自己
        startActivity(intent);
        finish();//关闭自己
        overridePendingTransition(0, 0); //去掉Activity切换间的动画
    }

    public void restart(View view) {
        ActivityManager manager = (ActivityManager)this.getSystemService(Context.ACTIVITY_SERVICE);
        manager.restartPackage("com.example.ui");
    }
}