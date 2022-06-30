package com.example.news.ui.user;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;

import java.lang.reflect.Field;
import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;

import com.example.news.ui.user.DataClass.StatcClass;
import com.example.news.ui.user.ToolClass.DataAccess;
import com.example.news.ui.user.ToolClass.L;
import com.example.news.R;
public class BaseActivity extends AppCompatActivity {

    public static Typeface typeFace;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //主题设置
        setTheme();

        //显示返回键
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }


        //取消任务栏（全屏）
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setTypeface();
    }

    //返回上一页
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // TODO Auto-generated method stub

        //android.R.id.home对应应用程序图标的id
        if(item.getItemId() == android.R.id.home)
        {
            finish();
            overridePendingTransition(0, 0);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Resources getResources() {
        float s = DataAccess.ReadDataFloat(BaseActivity.this,"size");
        L.e("---------" + s);
        //字体大小缩放
        Resources resources = super.getResources();
        Configuration configuration = resources.getConfiguration();
        if(s == 0.0){
            configuration.fontScale = StatcClass.Size;
        }else{
            configuration.fontScale = s;
        }
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
        return resources;
    }

    //字体设置(需要 ： <item name="android:typeface">serif</item>)
    public void setTypeface(){
        int fontsType = DataAccess.ReadDataInt(BaseActivity.this,"FONTS");
        if(fontsType == 0){
            typeFace = Typeface.createFromAsset(getAssets(), "fonts/FZHT.TTF");
        }else{
            switch (fontsType){
                case 1: typeFace = Typeface.createFromAsset(getAssets(), "fonts/FZHT.TTF");
                    break;
                case 2: typeFace = Typeface.createFromAsset(getAssets(), "fonts/FZXY.TTF");
                    break;
                case 3: typeFace = Typeface.createFromAsset(getAssets(), "fonts/HWXK.TTF");
                    break;
                case 4: typeFace = Typeface.createFromAsset(getAssets(), "fonts/HWXW.TTF");
                    break;
            }
        }
        try
        {
            Field field = Typeface.class.getDeclaredField("SERIF");
            field.setAccessible(true);
            field.set(null, typeFace);
        }
        catch (NoSuchFieldException e)
        {
            e.printStackTrace();
        }
        catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }
    }

    //APP主题设置
    public void setTheme(){
        int ThemeType = DataAccess.ReadDataInt(BaseActivity.this,"THEME");
        switch (ThemeType){
            case 0: setTheme(R.style.Theme_News);
                break;

        }
    }
}
