package com.example.news.ui.user.ToolClass;

import android.util.Log;

/**
 * 作者：ERP_LXKUN_JAK on 2019/3/8 11:41
 * 邮箱：2350785347@qq.com
 * 微信：DCT2350785347
 * 作用：Log的封装类
 * 用法：xxx
 */
public class L {

    //开关
    public static final boolean DEBUG = true;
    //TAG
    public static final String TAG = "Smartbutler";  // Smart butler： 智能滤镜

    //五个个等级 DIWEF
    public static void d(String text){
        if(DEBUG){
            Log.d(TAG,text);
        }
    }

    public static void i(String text){
        if(DEBUG){
            Log.i(TAG,text);
        }
    }

    public static void w(String text){
        if(DEBUG){
            Log.w(TAG,text);
        }
    }

    public static void e(String text){
        if(DEBUG){
            Log.e(TAG,text);
        }
    }
}
