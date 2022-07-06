package com.example.news;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class SelectBirthdayActivity extends AppCompatActivity {
    private EditText mEtBirthday;
    private DatePicker mDpBirthday;
    private Calendar cal;
    private Button mBtNext;
    private Button mBtCancel;
    private int year;
    private int month;
    private int day;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_birthday);

        //初始化组件
        mEtBirthday = (EditText) findViewById(R.id.mEtBirthday);
        mDpBirthday = (DatePicker) findViewById(R.id.mDpBirthday);
        mBtNext = (Button)findViewById(R.id.mBtNext);
        mBtCancel = (Button)findViewById(R.id.mBtCancel);

        //得到现在的时间
        cal = Calendar.getInstance();
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH)+1;//注意一月是从0开始算的
        day = cal.get(Calendar.DAY_OF_MONTH);

        mEtBirthday.setText(year+"-"+month+"-"+day);

        mDpBirthday.init(year, cal.get(Calendar.MONTH), day, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                int month_temp = monthOfYear+1;
                mEtBirthday.setText(year+"-"+month_temp+"-"+dayOfMonth);
            }
        });

        mBtNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //先将生日写入全局变量中
                String data = mEtBirthday.getText().toString().trim();
                NewsAPP mApp = (NewsAPP) getApplication();
                mApp.getMyUser().setBirthday(data);

                //跳转到选择频道
                Intent intent = new Intent(SelectBirthdayActivity.this, SelectMyTypeActivity.class) ;    //切换Login Activity至User Activity
                startActivity(intent);
                finish();
            }
        });

        mBtCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //先把全局变量中的值清空
                NewsAPP mApp = (NewsAPP) getApplication();
                mApp.clearUser();

                //跳转页面至登录页面
                Intent intent = new Intent(SelectBirthdayActivity.this,LoginActivity.class) ;    //切换Login Activity至User Activity
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
