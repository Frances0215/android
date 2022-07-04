package com.example.news;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class VoiceRecord {
    private AudioRecord audioRecord;
    private int recordBufsize = 0;
    private boolean isRecording = false;
    private Thread recordingThread;
    private static final String FILE_NAME = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC) + File.separator + "test.pcm";


    public static String getFileName() {
        return FILE_NAME;
    }

    public void createAudioRecord() {

        recordBufsize = AudioRecord
                .getMinBufferSize(16000,
                        AudioFormat.CHANNEL_IN_MONO,
                        AudioFormat.ENCODING_PCM_16BIT);
        Log.i("audioRecordTest", "size->" + recordBufsize);
        audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC,
                16000,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT,
                recordBufsize);
    }

    public void startRecord(String fileName) {
        if (isRecording) {
            return;
        }
        isRecording = true;
        if(audioRecord==null) {
            createAudioRecord();
        }
        audioRecord.startRecording();
        Log.i("audioRecordTest", "开始录音");
        recordingThread = new Thread(() -> {
            byte data[] = new byte[recordBufsize];
            File file = new File(FILE_NAME);
            FileOutputStream os = null;
            try {
                if (!file.exists()) {
                    file.createNewFile();
                    Log.i("audioRecordTest", "创建录音文件->" + FILE_NAME);
                }
                os = new FileOutputStream(file);//从头开始覆盖写入
            } catch (Exception e) {
                e.printStackTrace();
            }
            int read;
            if (os != null) {
                while (isRecording) {
                    read = audioRecord.read(data, 0, recordBufsize);
                    if (AudioRecord.ERROR_INVALID_OPERATION != read) {
                        try {
                            os.write(data);
                            Log.i("audioRecordTest", "写录音数据->" + read+ FILE_NAME);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            try {
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        recordingThread.start();
    }

    public void stopRecord() {
        isRecording = false;
        if (audioRecord != null) {
            audioRecord.stop();
            Log.i("audioRecordTest", "停止录音");
            audioRecord.release();
            audioRecord = null;
            recordingThread = null;
        }
    }


}
