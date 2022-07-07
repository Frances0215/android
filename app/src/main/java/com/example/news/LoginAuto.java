package com.example.news;

import android.app.Application;
import android.util.Log;

import cn.jiguang.verifysdk.api.JVerificationInterface;
import cn.jiguang.verifysdk.api.RequestCallback;

public class LoginAuto extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        JVerificationInterface.setDebugMode(true);
        JVerificationInterface.init(this, new RequestCallback<String>() {
            @Override
            public void onResult(int code, String result) {
                Log.d("MyApp", "[init] code = " + code + " result = " + result + " consists = " + (System.currentTimeMillis()));
            }
        });
    }
}