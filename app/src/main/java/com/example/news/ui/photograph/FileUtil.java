/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.example.news.ui.photograph;

import android.content.Context;
import android.util.Log;

import java.io.File;

public class FileUtil {
    public static File getSaveFile(Context context) {
        File file = new File(context.getExternalFilesDir(null).getPath()+"/"+"a.jpg");
        Log.v("FileUtil",file.getAbsolutePath());
        return file;
    }
}
