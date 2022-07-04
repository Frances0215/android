package com.example.news.ui.photograph;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.news.R;

public class PhotoResultActivity extends AppCompatActivity {

    private TextView mTvPhotoResult;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_result);
        mTvPhotoResult = (TextView) findViewById(R.id.mTvPhotoResult);

        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        String result=bundle.getString("result");
        mTvPhotoResult.setText(result);

    }


}
