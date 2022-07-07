package com.example.news;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


//import com.example.news.baselibrary.ScreenUtils;
//import com.example.news.baselibrary.ToastHelper;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.news.ui.home.SearchResultActivity;

import java.io.Closeable;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;

import cn.jiguang.net.HttpRequest;
import cn.jiguang.verifysdk.api.AuthPageEventListener;
import cn.jiguang.verifysdk.api.JVerificationInterface;
import cn.jiguang.verifysdk.api.JVerifyUIClickCallback;
import cn.jiguang.verifysdk.api.JVerifyUIConfig;
import cn.jiguang.verifysdk.api.VerifyListener;
import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.methods.CloseableHttpResponse;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.impl.client.CloseableHttpClient;
import cz.msebera.android.httpclient.impl.client.HttpClients;
import cz.msebera.android.httpclient.util.EntityUtils;

public class VerifyActivity extends AppCompatActivity {
    private static final String TAG = "VerifyActivity";
    private AlertDialog alertDialog;
    private int winWidth;
    private Button mBtLogin;
    private Button mBtLoginByCode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_auth);
        mBtLogin=(Button)findViewById(R.id.mBtLoginAuth);
        mBtLoginByCode=(Button)findViewById(R.id.mBtLoginByCode);
        mBtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginAuth(false);
            }
        });

        mBtLoginByCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_Login = new Intent(VerifyActivity.this,LoginActivity.class) ;
                startActivity(intent_Login);
                finish();
            }
        });
        //ScreenUtils.setStatusBarTransparent(getWindow());

        Display defaultDisplay = getWindowManager().getDefaultDisplay();
        Point point = new Point();
        defaultDisplay.getSize(point);
        if (point.x>point.y){
            winWidth =   point.y;
        }else {
            winWidth = point.x;
        }

        loginAuth(false);
    }

    private String getToken() {
        //final String[] token = new String[1];
        final String[] token = new String[1];;
        JVerificationInterface.getToken(this, new VerifyListener() {
            @Override
            public void onResult(int code, final String content, final String operator) {
                Log.e(TAG, "getToken result:"+code +",content:"+content+",operator:"+operator);
                token[0] =  content;
            }
        });
        return token[0];
    }

    private void loginAuth(boolean isDialogMode) {

        int result;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            result = checkSelfPermission(Manifest.permission.READ_PHONE_STATE);
            if (result != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "[2016],msg = 当前缺少权限", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        boolean verifyEnable = JVerificationInterface.checkVerifyEnable(this);
        if (!verifyEnable) {
            Toast.makeText(this, "[2016],msg = 当前网络环境不支持认证", Toast.LENGTH_SHORT).show();
            return;
        }
        
        JVerificationInterface.clearPreLoginCache();
        //showLoadingDialog();
        //final String[] loginToken = new String[1];
        setUIConfig(isDialogMode);
        //autoFinish 可以设置是否在点击登录的时候自动结束授权界面
        JVerificationInterface.loginAuth(this, true, new VerifyListener() {
            @Override
            public void onResult(final int code, final String content, final String operator) {
                Log.d(TAG, "[" + code + "]message=" + content + ", operator=" + operator);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dismissLoadingDialog();

                        if (code == Constants.CODE_LOGIN_SUCCESS) {
                            toSuccessActivity(Constants.ACTION_LOGIN_SUCCESS, content);
                            Log.e(TAG, "onResult: loginSuccess");
                        } else if (code != Constants.CODE_LOGIN_CANCELD) {
                            Log.e(TAG, "onResult: loginError");
                            toFailedActivigy(code, content);
                        }
                        getPhone(content);

                    }
                });
            }
        }, new AuthPageEventListener() {
            @Override
            public void onEvent(int cmd, String msg) {
                Log.d(TAG, "[onEvent]. [" + cmd + "]message=" + msg);
            }
        });


    }

    private void toSuccessActivity(int action, String token) {

        Intent intent = new Intent(this, SelectSexActivity.class);
        startActivity(intent);
    }

    private void toFailedActivigy(int code, String errorMsg) {
        String msg = errorMsg;
        if (code == 2003) {
            msg = "网络连接不通";
        } else if (code == 2005) {
            msg = "请求超时";
        } else if (code == 2016) {
            msg = "当前网络环境不支持认证";
        } else if (code == 2010) {
            msg = "未开启读取手机状态权限";
        } else if (code == 6001) {
            msg = "获取loginToken失败";
        } else if (code == 6006) {
            msg = "预取号结果超时，需要重新预取号";
        }
//        Intent intent = new Intent(this, cn.jiguang.demo.jverification.LoginResultActivity.class);
//        intent.putExtra(cn.jiguang.demo.jverification.Constants.KEY_ACTION, cn.jiguang.demo.jverification.Constants.ACTION_LOGIN_FAILED);
//        intent.putExtra(cn.jiguang.demo.jverification.Constants.KEY_ERORRO_MSG, msg);
//        intent.putExtra(cn.jiguang.demo.jverification.Constants.KEY_ERROR_CODE, code);
//        startActivity(intent);
    }

    private void setUIConfig(boolean isDialogMode) {
        JVerifyUIConfig portrait;
        JVerifyUIConfig landscape;
        if (isDialogMode) {
            portrait = getDialogPortraitConfig();
            landscape = getDialogLandscapeConfig();
        } else {
            portrait = getFullScreenPortraitConfig();
            landscape = getFullScreenLandscapeConfig();
        }
        //支持授权页设置横竖屏两套config，在授权页中触发横竖屏切换时，sdk自动选择对应的config加载。
        JVerificationInterface.setCustomUIWithConfig(portrait, landscape);
    }

    private JVerifyUIConfig getFullScreenPortraitConfig(){
        JVerifyUIConfig.Builder uiConfigBuilder = new JVerifyUIConfig.Builder();
        uiConfigBuilder.setSloganTextColor(0xFFD0D0D9);
        uiConfigBuilder.setLogoOffsetY(70);
        uiConfigBuilder.setNumFieldOffsetY(190);
        uiConfigBuilder.setPrivacyState(true);
        uiConfigBuilder.setLogoImgPath("jverification_demo_ic_icon");
        uiConfigBuilder.setLogoWidth(100);
        uiConfigBuilder.setLogoHeight(100);
        uiConfigBuilder.setNavTransparent(true);
//        uiConfigBuilder.setNavReturnImgPath("jverification_demo_btn_back");
        uiConfigBuilder.setCheckedImgPath(null);
        uiConfigBuilder.setNumberColor(0xFF222328);
        uiConfigBuilder.setLogBtnImgPath("selector_btn_normal");
        uiConfigBuilder.setLogBtnTextColor(0xFFFFFFFF);
        uiConfigBuilder.setLogBtnText("一键登录");
        uiConfigBuilder.setLogBtnOffsetY(255);
        uiConfigBuilder.setLogBtnWidth(300);
        uiConfigBuilder.setLogBtnHeight(55);
        uiConfigBuilder.setLogBtnTextSize(25);
        uiConfigBuilder.setAppPrivacyColor(0xFFBBBCC5,0xFF8998FF);
//        uiConfigBuilder.setPrivacyTopOffsetY(310);
        uiConfigBuilder.setPrivacyText("登录即同意《",""+""+"》并授权听闻获取本机号码");
        uiConfigBuilder.setPrivacyCheckboxHidden(true);
        uiConfigBuilder.setPrivacyTextCenterGravity(true);
        uiConfigBuilder.setPrivacyTextSize(12);
//        uiConfigBuilder.setPrivacyOffsetX(52-15);

        // 手机登录按钮
        RelativeLayout.LayoutParams layoutParamPhoneLogin = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParamPhoneLogin.setMargins(0, dp2Pix(this,360.0f),0,0);
        layoutParamPhoneLogin.addRule(RelativeLayout.ALIGN_PARENT_TOP,RelativeLayout.TRUE);
        layoutParamPhoneLogin.addRule(RelativeLayout.CENTER_HORIZONTAL,RelativeLayout.TRUE);
        TextView tvPhoneLogin = new TextView(this);
        tvPhoneLogin.setText("账号密码登录");
        tvPhoneLogin.setLayoutParams(layoutParamPhoneLogin);
        uiConfigBuilder.addCustomView(tvPhoneLogin, false, new JVerifyUIClickCallback() {
            @Override
            public void onClicked(Context context, View view) {
                toNativeVerifyActivity();
            }
        });

//        final View dialogViewTitle = LayoutInflater.from(getApplicationContext()).inflate(R.layout.jverification_demo_dialog_login_title,null, false);
//
//        uiConfigBuilder.addNavControlView(dialogViewTitle, new JVerifyUIClickCallback() {
//            @Override
//            public void onClicked(Context context, View view) {
//
//            }
//        });

//        final View dialogView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.jverification_demo_dialog_login_agreement,null, false);
//
//        dialogView.findViewById(R.id.dialog_login_no).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                JVerificationInterface.dismissLoginAuthActivity();
//            }
//        });
//
//        dialogView.findViewById(R.id.dialog_login_yes).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialogView.setVisibility(View.GONE);
//                dialogViewTitle.setVisibility(View.GONE);
//            }
//        });

//        dialogView.setVisibility(View.GONE);
//        dialogViewTitle.setVisibility(View.GONE);
//

//        uiConfigBuilder.addCustomView(dialogView, false, new JVerifyUIClickCallback() {
//            @Override
//            public void onClicked(Context context, View view) {
//            }
//        });


        return uiConfigBuilder.build();
    }

    private JVerifyUIConfig getFullScreenLandscapeConfig(){
        JVerifyUIConfig.Builder uiConfigBuilder = new JVerifyUIConfig.Builder();
        uiConfigBuilder.setStatusBarHidden(true);
        uiConfigBuilder.setSloganTextColor(0xFFD0D0D9);
        uiConfigBuilder.setSloganOffsetY(145);
        uiConfigBuilder.setLogoOffsetY(20);
        uiConfigBuilder.setNumFieldOffsetY(110);
        uiConfigBuilder.setPrivacyState(true);
        uiConfigBuilder.setLogoImgPath("jverification_demo_ic_icon");
        uiConfigBuilder.setNavTransparent(true);
        uiConfigBuilder.setNavReturnImgPath("jverification_demo_btn_back");
        uiConfigBuilder.setCheckedImgPath("jverification_demo_cb_chosen");
        uiConfigBuilder.setUncheckedImgPath("jverification_demo_cb_unchosen");
        uiConfigBuilder.setNumberColor(0xFF222328);
        uiConfigBuilder.setLogBtnImgPath("jverification_demo_selector_btn_main");
        uiConfigBuilder.setLogBtnTextColor(0xFFFFFFFF);
        uiConfigBuilder.setLogBtnText("一键登录");
        uiConfigBuilder.setLogBtnOffsetY(175);
        uiConfigBuilder.setLogBtnWidth(300);
        uiConfigBuilder.setLogBtnHeight(45);
        uiConfigBuilder.setAppPrivacyColor(0xFFBBBCC5,0xFF8998FF);
        uiConfigBuilder.setPrivacyText("登录即同意《"+""+"","》并授权听闻获取本机号码");
        uiConfigBuilder.setPrivacyCheckboxHidden(true);
        uiConfigBuilder.setPrivacyTextCenterGravity(true);
        uiConfigBuilder.setPrivacyTextSize(12);
//        uiConfigBuilder.setPrivacyOffsetX(52-15);
        uiConfigBuilder.setPrivacyOffsetY(18);

//        // 手机登录按钮
//        RelativeLayout.LayoutParams layoutParamPhoneLogin = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
//        layoutParamPhoneLogin.setMargins(0,  dp2Pix(this,15.0f),dp2Pix(this,15.0f),0);
//        layoutParamPhoneLogin.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,RelativeLayout.TRUE);
//        layoutParamPhoneLogin.addRule(RelativeLayout.ALIGN_PARENT_TOP,RelativeLayout.TRUE);
//        TextView tvPhoneLogin = new TextView(this);
//        tvPhoneLogin.setText("手机号码登录");
//        tvPhoneLogin.setLayoutParams(layoutParamPhoneLogin);
//        uiConfigBuilder.addNavControlView(tvPhoneLogin, new JVerifyUIClickCallback() {
//            @Override
//            public void onClicked(Context context, View view) {
//                toNativeVerifyActivity();
//            }
//        });
//
//        // 微信qq新浪登录
//
//        LinearLayout layoutLoginGroup = new LinearLayout(this);
//        RelativeLayout.LayoutParams layoutLoginGroupParam = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
//        layoutLoginGroupParam.setMargins(0,dp2Pix(this,235), 0,0);
//        layoutLoginGroupParam.addRule(RelativeLayout.ALIGN_PARENT_TOP,RelativeLayout.TRUE);
//        layoutLoginGroupParam.addRule(RelativeLayout.CENTER_HORIZONTAL,RelativeLayout.TRUE);
//        layoutLoginGroupParam.setLayoutDirection(LinearLayout.HORIZONTAL);
//        layoutLoginGroup.setLayoutParams(layoutLoginGroupParam);
//
//        ImageView btnWechat = new ImageView(this);
//        ImageView btnQQ = new ImageView(this);
//        ImageView btnXinlang = new ImageView(this);
//
//
//        btnWechat.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.e(TAG,"click wechat");
//            }
//        });
//        btnQQ.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.e(TAG,"click QQ");
//            }
//        });
//        btnXinlang.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.e(TAG,"click XinLang");
//            }
//        });
//
//
//        btnWechat.setImageResource(R.drawable.jverification_demo_o_wechat);
//        btnQQ.setImageResource(R.drawable.jverification_demo_o_qqx);
//        btnXinlang.setImageResource(R.drawable.jverification_demo_o_weibo);
//
//        LinearLayout.LayoutParams btnParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        btnParam.setMargins(25,0,25,0);
//
//        layoutLoginGroup.addView(btnWechat,btnParam);
//        layoutLoginGroup.addView(btnQQ,btnParam);
//        layoutLoginGroup.addView(btnXinlang,btnParam);
//        uiConfigBuilder.addCustomView(layoutLoginGroup, false, new JVerifyUIClickCallback() {
//            @Override
//            public void onClicked(Context context, View view) {
//            }
//        });
//
//
//
//        final View dialogViewTitle = LayoutInflater.from(getApplicationContext()).inflate(R.layout.jverification_demo_dialog_login_title,null, false);
//
//        uiConfigBuilder.addNavControlView(dialogViewTitle, new JVerifyUIClickCallback() {
//            @Override
//            public void onClicked(Context context, View view) {
//
//            }
//        });
//
//        final View dialogView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.jverification_demo_dialog_login_agreement_land,null, false);
//
//        dialogView.findViewById(R.id.dialog_login_no).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                JVerificationInterface.dismissLoginAuthActivity();
//            }
//        });
//
//        dialogView.findViewById(R.id.dialog_login_yes).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialogView.setVisibility(View.GONE);
//                dialogViewTitle.setVisibility(View.GONE);
//            }
//        });
//
//        uiConfigBuilder.addCustomView(dialogView, false, new JVerifyUIClickCallback() {
//            @Override
//            public void onClicked(Context context, View view) {
////                ToastUtil.showToast(MainActivity.this, "功能未实现", 1000);
//            }
//        });

        return uiConfigBuilder.build();
    }


    private JVerifyUIConfig getDialogPortraitConfig(){
        int widthDp = px2dip(this, winWidth);
        JVerifyUIConfig.Builder uiConfigBuilder = new JVerifyUIConfig.Builder().setDialogTheme(widthDp-60, 300, 0, 0, false);
//        uiConfigBuilder.setLogoHeight(30);
//        uiConfigBuilder.setLogoWidth(30);
//        uiConfigBuilder.setLogoOffsetY(-15);
//        uiConfigBuilder.setLogoOffsetX((widthDp-40)/2-15-20);
//        uiConfigBuilder.setLogoImgPath("logo_login_land");
        uiConfigBuilder.setLogoHidden(true);

        uiConfigBuilder.setNumFieldOffsetY(104).setNumberColor(Color.BLACK);
        uiConfigBuilder.setSloganOffsetY(135);
        uiConfigBuilder.setSloganTextColor(0xFFD0D0D9);
        uiConfigBuilder.setLogBtnOffsetY(161);

        uiConfigBuilder.setPrivacyOffsetY(15);
        uiConfigBuilder.setCheckedImgPath("jverification_demo_cb_chosen");
        uiConfigBuilder.setUncheckedImgPath("jverification_demo_cb_unchosen");
        uiConfigBuilder.setNumberColor(0xFF222328);
        uiConfigBuilder.setLogBtnImgPath("jverification_demo_selector_btn_main");
        uiConfigBuilder.setPrivacyState(true);
        uiConfigBuilder.setLogBtnText("一键登录");
        uiConfigBuilder.setLogBtnHeight(44);
        uiConfigBuilder.setLogBtnWidth(250);
        uiConfigBuilder.setAppPrivacyColor(0xFFBBBCC5,0xFF8998FF);
        uiConfigBuilder.setPrivacyText("登录即同意《"+""+"","》并授权听闻获取本机号码");
        uiConfigBuilder.setPrivacyCheckboxHidden(true);
        uiConfigBuilder.setPrivacyTextCenterGravity(true);
        uiConfigBuilder.setPrivacyTextSize(10);



        // 图标和标题
        LinearLayout layoutTitle = new LinearLayout(this);
        RelativeLayout.LayoutParams layoutTitleParam = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutTitleParam.setMargins(0,dp2Pix(this,50), 0,0);
        layoutTitleParam.addRule(RelativeLayout.ALIGN_PARENT_TOP,RelativeLayout.TRUE);
        layoutTitleParam.addRule(RelativeLayout.CENTER_HORIZONTAL,RelativeLayout.TRUE);
        layoutTitleParam.setLayoutDirection(LinearLayout.HORIZONTAL);
        layoutTitle.setLayoutParams(layoutTitleParam);

        ImageView img = new ImageView(this);
//        img.setImageResource(R.drawable.jverification_demo_logo_login_land);
        TextView tvTitle = new TextView(this);
        tvTitle.setText("极光认证");
        tvTitle.setTextSize(19);
        tvTitle.setTextColor(Color.BLACK);
        tvTitle.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));

        LinearLayout.LayoutParams imgParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        imgParam.setMargins(0,0,dp2Pix(this,6),0);
        LinearLayout.LayoutParams titleParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        imgParam.setMargins(0,0,dp2Pix(this,4),0);
        layoutTitle.addView(img,imgParam);
        layoutTitle.addView(tvTitle,titleParam);
        uiConfigBuilder.addCustomView(layoutTitle,false,null);

        // 关闭按钮
        ImageView closeButton = new ImageView(this);

        RelativeLayout.LayoutParams mLayoutParams1 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mLayoutParams1.setMargins(0, dp2Pix(this,10.0f),dp2Pix(this,10),0);
        mLayoutParams1.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,RelativeLayout.TRUE);
        mLayoutParams1.addRule(RelativeLayout.ALIGN_PARENT_TOP,RelativeLayout.TRUE);
        closeButton.setLayoutParams(mLayoutParams1);
//        closeButton.setImageResource(R.drawable.jverification_demo_btn_close);
        uiConfigBuilder.addCustomView(closeButton, true, null);

        return uiConfigBuilder.build();
    }

    private JVerifyUIConfig getDialogLandscapeConfig(){
        int widthDp = px2dip(this, winWidth);
        JVerifyUIConfig.Builder uiConfigBuilder = new JVerifyUIConfig.Builder().setDialogTheme(480, widthDp-100, 0, 0, false);
//        uiConfigBuilder.setLogoHeight(40);
//        uiConfigBuilder.setLogoWidth(40);
//        uiConfigBuilder.setLogoOffsetY(-15);
//        uiConfigBuilder.setLogoImgPath("logo_login_land");
        uiConfigBuilder.setLogoHidden(true);

        uiConfigBuilder.setNumFieldOffsetY(104).setNumberColor(Color.BLACK);
        uiConfigBuilder.setNumberSize(22);
        uiConfigBuilder.setSloganOffsetY(135);
        uiConfigBuilder.setSloganTextColor(0xFFD0D0D9);
        uiConfigBuilder.setLogBtnOffsetY(161);

        uiConfigBuilder.setPrivacyOffsetY(15);
        uiConfigBuilder.setCheckedImgPath("jverification_demo_cb_chosen");
        uiConfigBuilder.setUncheckedImgPath("jverification_demo_cb_unchosen");
        uiConfigBuilder.setNumberColor(0xFF222328);
        uiConfigBuilder.setLogBtnImgPath("jverification_demo_selector_btn_main");
        uiConfigBuilder.setPrivacyState(true);
        uiConfigBuilder.setLogBtnText("一键登录");
        uiConfigBuilder.setLogBtnHeight(44);
        uiConfigBuilder.setLogBtnWidth(250);
        uiConfigBuilder.setAppPrivacyColor(0xFFBBBCC5,0xFF8998FF);
        uiConfigBuilder.setPrivacyText("登录即同意《"+""+"","》并授权听闻获取本机号码");
        uiConfigBuilder.setPrivacyCheckboxHidden(true);
        uiConfigBuilder.setPrivacyTextCenterGravity(true);
//        uiConfigBuilder.setPrivacyOffsetX(52-15);
        uiConfigBuilder.setPrivacyTextSize(10);

        // 图标和标题
        LinearLayout layoutTitle = new LinearLayout(this);
        RelativeLayout.LayoutParams layoutTitleParam = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutTitleParam.setMargins(dp2Pix(this,20),dp2Pix(this,15), 0,0);
        layoutTitleParam.addRule(RelativeLayout.ALIGN_PARENT_TOP,RelativeLayout.TRUE);
        layoutTitleParam.setLayoutDirection(LinearLayout.HORIZONTAL);
        layoutTitle.setLayoutParams(layoutTitleParam);

        ImageView img = new ImageView(this);
//        img.setImageResource(R.drawable.jverification_demo_logo_login_land);
        TextView tvTitle = new TextView(this);
        tvTitle.setText("极光认证");
        tvTitle.setTextSize(19);
        tvTitle.setTextColor(Color.BLACK);
        tvTitle.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));

        LinearLayout.LayoutParams imgParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        imgParam.setMargins(0,0,dp2Pix(this,6),0);
        LinearLayout.LayoutParams titleParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        imgParam.setMargins(0,0,dp2Pix(this,4),0);
        layoutTitle.addView(img,imgParam);
        layoutTitle.addView(tvTitle,titleParam);
        uiConfigBuilder.addCustomView(layoutTitle,false,null);


        // 关闭按钮
        ImageView closeButton = new ImageView(this);

        RelativeLayout.LayoutParams mLayoutParams1 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mLayoutParams1.setMargins(0, dp2Pix(this,10.0f),dp2Pix(this,10),0);
        mLayoutParams1.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,RelativeLayout.TRUE);
        mLayoutParams1.addRule(RelativeLayout.ALIGN_PARENT_TOP,RelativeLayout.TRUE);
        closeButton.setLayoutParams(mLayoutParams1);
     //   closeButton.setImageResource(R.drawable.jverification_demo_btn_close);
        uiConfigBuilder.addCustomView(closeButton, true, null);

        return uiConfigBuilder.build();
    }

    private void toNativeVerifyActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }


    public void showLoadingDialog() {
        dismissLoadingDialog();
        alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setCancelable(false);
        alertDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                return keyCode == KeyEvent.KEYCODE_SEARCH || keyCode == KeyEvent.KEYCODE_BACK;
            }
        });
        alertDialog.show();
        alertDialog.setContentView(R.layout.jverification_demo_loading_alert);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //一定要在setContentView之后调用，否则无效
        alertDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }

    public void dismissLoadingDialog() {
        if (null != alertDialog && alertDialog.isShowing()) {
            alertDialog.dismiss();
        }
    }

    private int dp2Pix(Context context, float dp) {
        try {
            float density = context.getResources().getDisplayMetrics().density;
            return (int) (dp * density + 0.5F);
        } catch (Exception e) {
            return (int) dp;
        }
    }

    private int px2dip(Context context, int pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    //用于将语音输入，写入到搜索框
    final Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    String phone = (String) msg.obj;
                    Log.e("手机号：",phone);
                    break;
                default:
                    break;
            }
        }

    };

    private void getPhone(String loginToken)  {

        new Thread(new Runnable() {
            @Override
            public void run() {
                String prikey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBANpOlWZincwOTGSo"+
                        "4xb767VDCTae482gUnA2sX0PJ/PsarhPIxqkIKi3DTAnCFyr3xG6+nRN717d+wSx"+
                        "8j4io0IgeIiFmLYw85byGQ7jU+KTlprklZvwWryyDmTqi/7tHNUQF2rLJZ8yQOQh"+
                        "CPwHTEcH0GJZ4jf5FEVQS8nGlyaZAgMBAAECgYA3YqaOwkhMg/gntZ1fsGxdoXNN"+
                        "80PSYHFp3MZs9xJo1TtCJXm/gZJzm3VyGQULFePTSL/QEBHB3MmZqFfQIdOPNF7w"+
                        "kxy6gUfivFtq188gB16imJxDhsk3M0SayicC+0O5Ekp7c6UCii9g1R/kBRlK5f1E"+
                        "6u+CsC2Ezd4uNcEfcQJBAO2SzEDTYuDn0HKwI1mi3lwK8hYqahNuGQOOYdfOIcQH"+
                        "DtxsU8WlFInfPVSEsQtdIbD+SoM3j+uItDwzRvq3SS0CQQDrPTxCmF+Fpq5GaLtz"+
                        "XehrHTRTsbmFpQ+DJWbgxGfJoWLFJIb38iqAsK6oqYkAgI1DHEKbdb51tzdq+q+5"+
                        "jB6dAkEAxkghJXHIMwIHXdFYj7V5dMTF7G4V/oWDZw5s5xrp35wscRgQiwMOs0uY"+
                        "+nOM+HkQZ5K1SfMTsXd1RlFdyl8zOQJAIW2dP5VplZN/FnBRmu5QVdBbnD0YMP6o"+
                        "uqk4+l3Opd9yKrOrHlbiZVE2MK+O0WxzJoNAj9jmjNHRZTlkeUiJHQJAXbh4hEgL"+
                        "bbIcJmh9ls5DMk+RFgGLR0U3+dLw6TLW6DuvlVr6aBYhs7Z+wZNcxbxu4TTvAzASyo6go1KktYWp3A==";

                //String loginToken=getToken();
                String JSONBody="{"+"\"loginToken\":\""+loginToken+"\"}";
                Log.e("JSONBody:",JSONBody);
                String url =  "https://api.verification.jpush.cn/v1/web/loginTokenVerify";
                String appkey="1faeb1cb23ceb192df0fa2ce";
                String masterKey="01345bbf6d08d3522dae59d4";
                String content = doPostForJpush(url,JSONBody,appkey,masterKey);
                Log.e("返回的content:",content);

                String[] strs = content.split(",");//根据，切分字符串
                Log.e("phone:",strs[4]);
                String[]str2 = strs[4].split("\"");
                Log.e("phone:",str2[3]);

                String phone="";
                try{
                    String code = decrypt(str2[3],prikey);
                     phone = code.substring(code.length()-11);
                }catch (Exception e){
                    e.printStackTrace();
                }
                Message message = new Message();
                message.obj =phone;
                message.what = 0;
                mHandler.sendMessage(message);
            }
        }).start();

    }



        /**

         获取phone

         @param loginToken

         @return

         @throws Exception
         */
        /**
         * @Description  极光推送专用post
         * @Author PrinceCharmingDong
         * @Date 2020/3/4
         */
        public  String doPostForJpush (String url, String JSONBody,String appKey,String masterKey) {
            CloseableHttpResponse response = null;
            CloseableHttpClient httpClient = null;
            String responseContent = "";
            try {
                httpClient = HttpClients.createDefault();
                HttpPost httpPost = new HttpPost(url);
                httpPost.addHeader("Content-Type", "application/json");
                httpPost.addHeader("Authorization", "Basic " + Base64.getUrlEncoder()
                        .encodeToString((appKey+ ":" + masterKey).getBytes()));
                httpPost.setEntity(new StringEntity(JSONBody));
                response = httpClient.execute(httpPost);
                HttpEntity entity = response.getEntity();
                responseContent = EntityUtils.toString(entity, "UTF-8");
                response.close();
                httpClient.close();
                System.out.println(responseContent);
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                close(response, httpClient);
            }
            return responseContent;
        }



        private  void close(Closeable... closeables) {
            if (closeables != null && closeables.length > 0) {
                try {
                    for (Closeable closeable : closeables) {
                        if (closeable != null) {
                            closeable.close();
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    public static String decrypt(String cryptograph, String prikey) throws Exception {
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(prikey));
        PrivateKey privateKey = KeyFactory.getInstance("RSA").generatePrivate(keySpec);

        Cipher cipher=Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);

        byte [] b = Base64.getDecoder().decode(cryptograph);
        return new String(cipher.doFinal(b));
    }

    //}



    @Override
    protected void onDestroy() {
        super.onDestroy();
        dismissLoadingDialog();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        loginAuth(false);
    }
}
