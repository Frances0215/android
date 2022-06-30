package com.example.news.ui.user.ToolClass;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public class DataAccess  {

/** Int **/
    //储存数据
    public static void SaveDataInt(Context context, String Key, int num){
        //获取SharedPreferences对象
        SharedPreferences sp01 = context.getSharedPreferences("SP01", MODE_PRIVATE);
        //存入数据
        SharedPreferences.Editor editor01 = sp01.edit();
        editor01.putInt(Key, num);
        editor01.apply();   //储存信息
    }

    //读取数据
    public static int ReadDataInt(Context context, String Key){
        //获取SharedPreferences对象
        SharedPreferences sp01 = context.getSharedPreferences("SP01", MODE_PRIVATE);
        int num = sp01.getInt(Key,0);
        return num;
    }

/** String **/
    public static void SaveDataString(Context context, String Key, String data){
        SharedPreferences sp01 = context.getSharedPreferences("SP01", MODE_PRIVATE);
        SharedPreferences.Editor editor01 = sp01.edit();
        editor01.putString(Key, data);
        editor01.apply();
    }

    public static String ReadDataString(Context context, String Key){
        SharedPreferences sp01 = context.getSharedPreferences("SP01", MODE_PRIVATE);
        String num = sp01.getString(Key,"null");
        return num;
    }

/** Float **/
    public static void SaveDataFloat(Context context, String Key, float data){
        SharedPreferences sp01 = context.getSharedPreferences("SP01", MODE_PRIVATE);
        SharedPreferences.Editor editor01 = sp01.edit();
        editor01.putFloat(Key, data);
        editor01.apply();
    }

    public static float ReadDataFloat(Context context, String Key){
        SharedPreferences sp01 = context.getSharedPreferences("SP01", MODE_PRIVATE);
        float num = sp01.getFloat(Key,0.0f);
        return num;
    }

    //删除指定Key数据，当 KEY = ALL 时清空所有数据
    public static void DeleteData(Context context, String Key){
        //获取SharedPreferences对象
        SharedPreferences sp01 = context.getSharedPreferences("SP01", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp01.edit();
        if(Key.equals("ALL")){
            editor.clear();
        }else{
            editor.remove(Key);
        }
        editor.commit();
    }
}
