

package com.example.news.ui.photograph;

import static com.amap.api.maps.model.BitmapDescriptorFactory.getContext;
import static cn.com.chinatelecom.account.api.CtAuth.mContext;

import android.Manifest;
import android.app.Activity;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.core.app.ActivityCompat;
//import android.support.v4.app.ActivityCompat;
//import android.support.v4.content.ContextCompat;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.BackgroundColorSpan;
import android.util.Log;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.example.news.DropDownEditText;
import com.example.news.FontIconView;
import com.example.news.NewsAPP;
import com.example.news.R;
import com.example.news.VoiceRecord;
import com.example.news.VoiceTrans;
import com.example.news.ui.home.HomeFragment;
import com.example.news.ui.home.MyNewsListAdapter;
import com.example.news.ui.home.SearchResultActivity;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechEvent;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.SynthesizerListener;



import com.amap.api.maps.AMapException;
import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.AMapNaviListener;
import com.amap.api.navi.AMapNaviView;
import com.amap.api.navi.AMapNaviViewListener;
import com.amap.api.navi.ParallelRoadListener;
import com.amap.api.navi.enums.AMapNaviParallelRoadStatus;
import com.amap.api.navi.model.AMapCalcRouteResult;
import com.amap.api.navi.model.AMapLaneInfo;
import com.amap.api.navi.model.AMapModelCross;
import com.amap.api.navi.model.AMapNaviCameraInfo;
import com.amap.api.navi.model.AMapNaviCross;
import com.amap.api.navi.model.AMapNaviLocation;
import com.amap.api.navi.model.AMapNaviRouteNotifyData;
import com.amap.api.navi.model.AMapNaviTrafficFacilityInfo;
import com.amap.api.navi.model.AMapServiceAreaInfo;
import com.amap.api.navi.model.AimLessModeCongestionInfo;
import com.amap.api.navi.model.AimLessModeStat;
import com.amap.api.navi.model.NaviInfo;
import com.amap.api.navi.model.NaviLatLng;
import com.example.news.util.TTSController;
import com.amap.api.maps.AMapException;
import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.NaviSetting;
import com.amap.api.navi.enums.NaviType;
import com.amap.api.navi.enums.TravelStrategy;
import com.amap.api.navi.model.AMapCalcRouteResult;
import com.amap.api.navi.model.NaviLatLng;
import com.amap.api.navi.model.NaviPoi;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Poi;
import com.amap.api.navi.view.PoiInputItemWidget;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class SearchActivity extends Activity implements PoiSearch.OnPoiSearchListener
{

    //请求权限码
    private static final int REQUEST_PERMISSIONS = 9527;
    /***
     *
     搜索变量创建
     */
    private String city = "成都市";
    private PoiSearch mSearch;//Poi（高德地图点）搜索对象
    private ArrayList<AddressBean> data;//储存搜索到的地图点

    /*****
     *
     *
     定位变量创建
     */

    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明定位回调监听器
    public AMapLocationClientOption mLocationOption = null;

    protected NaviPoi startPoi;
    protected NaviLatLng start;

    private ListView mLvGuide;
    private FontIconView mFiVoice;
    private DropDownEditText mEtSearch;

    private VoiceTrans myTrans = new VoiceTrans();
    private VoiceRecord myVoice = new VoiceRecord();

    private MyGuideListAdapter guideListAdapter;
    private int isLongClick=0;
    public AMapLocationListener mLocationListener = new AMapLocationListener() {
        /**
         * 接收异步返回的定位结果
         *
         * @param aMapLocation
         */
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            if (aMapLocation != null) {
                if (aMapLocation.getErrorCode() == 0) {
                    //地址

                    city=aMapLocation.getCity();//获取城市名
                    String Poiname=aMapLocation.getPoiName();//获取Poi地点名
                    double lat=aMapLocation.getLatitude();//获取纬度
                    double lon=aMapLocation.getLongitude();//获取经度
                    //赋值给出发点
                    start=new NaviLatLng(lat,lon);
                    startPoi=new NaviPoi(Poiname, new LatLng(lat,lon), null);
                } else {
                    //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                    Log.e("AmapError", "location Error, ErrCode:"
                            + aMapLocation.getErrorCode() + ", errInfo:"
                            + aMapLocation.getErrorInfo());
                }
            }
        }

    };


    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //改变通知栏的颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }

        setContentView(R.layout.activity_guide);
        initLocation();//初始化定位
        checkingAndroidVersion();//获取权限

        //初始化变量
        mLvGuide = (ListView)findViewById(R.id.mLvGuide);
        mFiVoice = (FontIconView)findViewById(R.id.mFiVoice);
        mEtSearch = (DropDownEditText)findViewById(R.id.mEtSearch);
        //输入关键词
        //初始化语音输入,语音输入完之后直接进行搜索
        initVoice();

        //搜索按钮
        mEtSearch.setOnDropArrowClickListener(new DropDownEditText.OnDropArrowClickListener() {
            @Override
            public void onDropArrowClick() {
                mEtSearch.setFocusableInTouchMode(false);
                mEtSearch.setFocusable(false);
                String str = String.valueOf(mEtSearch.getText());
                initSearch(str);
                initListGuide();
            }
        });

    }

    //语音输入结果分析
    //用于将语音输入，写入到搜索框
    final Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    String searchFor = (String) msg.obj;
                    String str=searchFor.replaceAll("[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……& amp;*（）——+|{}【】‘；：”“’。，、？|-]", "");


                        mEtSearch.setText(str);
                        //Toast.makeText(getContext(), "语音输入已完成可以按搜索键开始查询了",Toast.LENGTH_SHORT).show();
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        View view2 = View.inflate(getContext(), R.layout.voice_input, null);
                        TextView mTvVoice = view2.findViewById(R.id.mTvVoice);
                        if(str.equals("")){
                            mTvVoice.setText("空");
                        }else {
                            mTvVoice.setText(str);
                        }

                        //弹窗显示
                    builder.setView(view2).setPositiveButton("开始查询", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //用户点击开始查询之后，才开始查询
                            initSearch(str);
                            initListGuide();
                        }
                    }).setNegativeButton("取消查询", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //不做处理
                        }
                    }).setTitle("语音输入结果为");

                    builder.create().show();

                    break;
                default:
                    break;
            }
        }

    };


    //语音输入
    private void initVoice(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1000);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, 2000);
        }

        //语音输入开始
        mFiVoice.setOnLongClickListener(new startRecordListener());
        mFiVoice.setOnClickListener(new stopRecordListener());

    }

    //长按录音，松开后自动执行短按操作
    class startRecordListener implements View.OnLongClickListener {
        @Override
        public boolean onLongClick(View v) {
            isLongClick = 1;
            Vibrator vib = (Vibrator)getSystemService(Service.VIBRATOR_SERVICE);
            vib.vibrate(20);//只震动一秒，一次
            myVoice.startRecord(myVoice.getFileName());
            return false; //KeyPoint：setOnLongClickListener中return的值决定是否在长按后再加一个短按动作，true为不加短按,false为加入短按
        }
    }
    //短按停止录音，直接点击短按无效
    class stopRecordListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if(isLongClick==1){
                myVoice.stopRecord();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            v.announceForAccessibility("正在分析语音");
                            String result = myTrans.voiceTrans(myVoice.getFileName());
                            //String result = "哈哈";
                            Message message = new Message();
                            message.obj =result;
                            message.what = 0;
                            mHandler.sendMessage(message);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
            isLongClick=0;

        }
    }

    private void initSearch(String voiceResult){
        try {
            PoiSearch.Query query = new PoiSearch.Query(voiceResult, "", city);//("搜索关键字"，“不填”，“城市,不填为全国范围”)
            mSearch = new PoiSearch(getApplicationContext(), query);//创建搜索对象
            //设置异步监听，在onPoiSearched()函数中处理结果
            mSearch.setOnPoiSearchListener(this);
            //查询POI异步接口
            mSearch.searchPOIAsyn();
        } catch (com.amap.api.services.core.AMapException e) {
            e.printStackTrace();
            Log.i("SearchERROR","SearchERROR");
        }
    }

    @Override
    public void onPoiSearched(PoiResult poiResult, int rCode) {
        Log.v("onPoiSearched","onPoiSearched");
        //搜索回调
        if(poiResult != null){
            data = new ArrayList<AddressBean>();//AddressBean储存地图点信息
            ArrayList<PoiItem> items = poiResult.getPois();
            for(PoiItem item : items){
                //获取经纬度对象
                LatLonPoint llp = item.getLatLonPoint();
                double lon = llp.getLongitude();
                double lat = llp.getLatitude();
                //获取标题
                String title = item.getTitle();
                //获取内容
                String text = item.getSnippet();
                //获取Poi ID
                String Poi=item.getPoiId();
                data.add(new AddressBean(lon, lat, title, text,Poi));
            }

        }

    }

    private void initListGuide(){
        guideListAdapter = new MyGuideListAdapter(this,R.layout.activity_guidelist_item,data);
        mLvGuide.setAdapter(guideListAdapter);
    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }




    /**
     * 动态请求权限
     */
    @AfterPermissionGranted(REQUEST_PERMISSIONS)
    private void requestPermission() {
        String[] permissions = {
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };

        if (EasyPermissions.hasPermissions(this, permissions)) {
            //true 有权限 开始定位
            Log.v("已获得权限，可以定位啦！","已获得权限");
            mLocationClient.startLocation();
        } else {
            //false 无权限
            EasyPermissions.requestPermissions(this, "需要权限", REQUEST_PERMISSIONS, permissions);
        }
    }

    /**
     * 请求权限结果
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //设置权限请求结果
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
    /**
     * 检查Android版本
     */
    private void checkingAndroidVersion() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            //Android6.0及以上先获取权限再定位
            requestPermission();
        }else {
            //Android6.0以下直接定位
            //启动定位
            mLocationClient.startLocation();
        }
    }

    //初始化定位
    private void initLocation() {
        try {
            mLocationClient = new AMapLocationClient(getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);
        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();
        //获取一次定位结果：
        mLocationOption.setOnceLocation(true);
        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //获取最近3s内精度最高的一次定位结果：
        //设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会，默认为false。
        mLocationOption.setOnceLocationLatest(true);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //设置定位请求超时时间，单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒。
        mLocationOption.setHttpTimeOut(20000);
        //关闭缓存机制，高精度定位会产生缓存。
        mLocationOption.setLocationCacheEnable(false);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
    }

    @Override
    public void onResume()
    {
        super.onResume();

    }

    @Override
    public void onPause()
    {
        super.onPause();

    }

    @Override

    protected void onDestroy() {

        super.onDestroy();
        mLocationClient.onDestroy();////销毁定位客户端，同时销毁本地定位服务。

    }

}
