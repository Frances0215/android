package com.example.news.ui.photograph;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.news.R;

public class PhotoResultActivity extends AppCompatActivity {

    private TextView mTvPhotoResult;
    private Button mBtTake;
    private Button mBtCancel;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_result);
        mTvPhotoResult = (TextView) findViewById(R.id.mTvPhotoResult);
        mBtCancel = (Button)findViewById(R.id.mBtCancel);
        mBtTake = (Button)findViewById(R.id.mBtTake);

        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        String result=bundle.getString("result");
        String str = result+"已经读完啦，点击右下方按钮再拍一张吧";
        mTvPhotoResult.setText(str);
        //拍照
        mBtTake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //取消拍照，返回上一个activity
        mBtCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

//用于添加上方标题栏中的返回按钮1
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

    }
    //用于添加上方标题栏中的返回按钮2
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish(); // back button
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
