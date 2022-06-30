package com.example.news.ui.photograph;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

public class ImageUtil {

    //把图片变成字符串
    public static String imageToBase64(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream); //图片压缩变成输出流
        byte[] buffer = byteArrayOutputStream.toByteArray();
        String baseStr = Base64.encodeToString(buffer,Base64.DEFAULT); //Base64算法
        return baseStr;
    }

    //把字符串还原成图片
    public static Bitmap base64ToImage(String bitmap64){
        if(bitmap64 != null){
            byte[] bytes = Base64.decode(bitmap64,Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
            return bitmap;
        }
        return null;
    }
}
