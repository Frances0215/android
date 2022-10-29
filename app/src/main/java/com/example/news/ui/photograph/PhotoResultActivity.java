package com.example.news.ui.photograph;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.baidu.ocr.sdk.OCR;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.AccessToken;
import com.example.news.R;

import java.io.File;
import java.io.FileInputStream;


public class PhotoResultActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_GENERAL_BASIC = 106;
    private static String UriPath;

    private TextView mTvPhotoResult;
    private Button mBtTake;
    private Button mBtCancel;
    private boolean hasGotToken=false;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //改变通知栏的颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }

        setContentView(R.layout.activity_photo_result);
        mTvPhotoResult = (TextView) findViewById(R.id.mTvPhotoResult);
        mBtCancel = (Button)findViewById(R.id.mBtCancel);
        mBtTake = (Button)findViewById(R.id.mBtTake);
        initAccessToken();
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        String result=bundle.getString("result");
        String str = result+"\n已经读完啦，点击右下方按钮再拍一张吧";
        mTvPhotoResult.setText(str);
        //拍照
        mBtTake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                takePhoto(v);
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

    public void takePhoto(View view) {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED){
            //执行拍照
            if(hasGotToken){
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                UriPath= Environment.getExternalStorageDirectory().getPath();
                UriPath=UriPath+"/"+"temp.png";
                Log.v("OUTPUT",UriPath);
                File file=new File(UriPath);
                if(Build.VERSION.SDK_INT < 24){
                    Uri photoUri=Uri.fromFile(new File(UriPath));
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                    startActivityForResult(intent, REQUEST_CODE_GENERAL_BASIC);
                }
                else {
//                Uri photoUri=Uri.fromFile(new File(UriPath));
                    Uri photoUri = FileProvider.getUriForFile(this.getApplicationContext(), "com.example.news.fileprovider", file);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);

//                intent.putExtra(MediaStore,FileUtil.getSaveFile(getActivity().getApplicationContext()).getAbsolutePath());
                    startActivityForResult(intent, REQUEST_CODE_GENERAL_BASIC);
                }
//                Intent intent = new Intent(this, CameraActivity.class);
//                intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,
//                        FileUtil.getSaveFile(getApplicationContext()).getAbsolutePath());
//                intent.putExtra(CameraActivity.KEY_CONTENT_TYPE,
//                        CameraActivity.CONTENT_TYPE_GENERAL);
//                startActivityForResult(intent, REQUEST_CODE_GENERAL_BASIC);
            }
            else {
                Log.v("ERROR","NO LIENCE");
            }

//            takePic();
        }else{
            //去申请权限
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},1);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.v("phtograph", "enterFile");

        if (resultCode == Activity.RESULT_OK) {
            String sdStatus = Environment.getExternalStorageState();

            if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) {
                System.out.println(" ------------- sd card is not avaiable ---------------");
                return;
            }
            FileInputStream fis = null;
            RecognizeService.recAccurateBasic(this.getApplicationContext(), UriPath,
                    new RecognizeService.ServiceListener() {
                        @Override
                        public void onResult(String result) {
                            String d = "";
                            String[] tempa = result.split("]");
                            String[] temp = tempa[0].split("\"words\":");
                            if (temp.length > 1) {

                                for (int i = 1; i < temp.length; i++) {
                                    d = d + temp[i];
                                }
                                d = d.replace("\"", "");
                                d = d.replace("{", "");
                                d = d.replace(",", "");
                                d = d.replace("}", "");
                            } else {
                                d = "无结果";
                            }

                            infoPopText(d);
                        }
                    });
        }
    }

    private void infoPopText(final String result) {
        Intent intent = new Intent(this, PhotoResultActivity.class);
        Bundle bundle=new Bundle();
        bundle.putString("result",result);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }

    private void initAccessToken() {
        OCR.getInstance(this.getApplicationContext()).initAccessToken(new OnResultListener<AccessToken>() {
            @Override
            public void onResult(AccessToken accessToken) {
                String token = accessToken.getAccessToken();
                hasGotToken = true;
            }

            @Override
            public void onError(OCRError error) {
                error.printStackTrace();
                Log.v("licence方式获取token失败", error.getMessage());
            }
        }, this.getApplicationContext());
    }
}



