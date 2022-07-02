package com.example.news.ui.photograph;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.news.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class PhotographFragment extends Fragment  {

    private PhotographViewModel photographViewModel;

    private FrameLayout mFlCamera;

    private ImageView mPicture;
    private Context mContext = null;
    private static final int REQUEST_CODE_TAKE = 1;
    private static final int REQUEST_CODE_CHOOSE = 0;
    private Uri imageUri; //定位资源位置
    private String imageBase64;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        photographViewModel =
                new ViewModelProvider(this).get(PhotographViewModel.class);
        View root = inflater.inflate(R.layout.fragment_photograph, container, false);

        photographViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s){
            }
        });
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mFlCamera=view.findViewById(R.id.mFlCamera);
        mPicture = view.findViewById(R.id.mPicture);
        mFlCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto(v);
            }
        });
    }



    public void takePhoto(View view) {
        if(ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED){
            //执行拍照
            doTake();
        }else{
            //去申请权限
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.CAMERA},1);
        }
    }

    //接收权限申请的结果
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==1){
            if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                doTake();
            }else{
                Toast.makeText(getActivity(),"您未获得摄像头权限！",Toast.LENGTH_SHORT).show();
            }
        }else if(requestCode == 0){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                openAlbum();
            }else{
                Toast.makeText(getActivity(),"您未获得访问相册权限！",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void doTake() {
        File imageTemp = new File(getActivity().getExternalCacheDir(),"imageOut.jpeg");
        if(imageTemp.exists()){
            imageTemp.delete();
        }
        try {
            imageTemp.createNewFile();
        }catch (IOException e){
            e.printStackTrace();
        }
        imageUri = Uri.fromFile(imageTemp);
        if(Build.VERSION.SDK_INT > 24){
            //contentProvider
            imageUri = FileProvider.getUriForFile(getActivity(),"com.example.ui.fileprovider",imageTemp);
        }else{
            imageUri = Uri.fromFile(imageTemp);
        }
        //传递文件路径
        Intent intent = new Intent();
        intent.setAction("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);      //接收照片
        startActivityForResult(intent,REQUEST_CODE_TAKE);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_CODE_TAKE){
            if(resultCode==RESULT_OK){
                //获取拍摄的照片
                try {
                    InputStream inputStream = getActivity().getContentResolver().openInputStream(imageUri);
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    mPicture.setImageBitmap(bitmap);//显示头像
//                    //保存图片
                    String imageToBase64 = ImageUtil.imageToBase64(bitmap);
                    imageBase64 = imageToBase64;

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }else if(requestCode == REQUEST_CODE_CHOOSE){ //获取相册中的图片
            if(Build.VERSION.SDK_INT < 19){
                try {
                    handleImageBeforeApi19(data);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }else{
                try {
                    handleImageOnApi19(data);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    public static boolean getRootPath(Context context) {

// 是否有SD卡

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)

                || !Environment.isExternalStorageRemovable()) {

            //return context.getExternalCacheDir().getPath(); // 有
            return true;

        } else {

            // return context.getCacheDir().getPath(); // 无
            return false;
        }

    }

    private void handleImageBeforeApi19(Intent data) throws FileNotFoundException {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri,null);
        displayImage(imagePath);
    }

    @TargetApi(19)
    private void handleImageOnApi19(Intent data) throws FileNotFoundException { //通过uri拿到对应路径
        String imagePath = null;
        Uri uri = data.getData();
        if(DocumentsContract.isDocumentUri(getActivity(),uri)){
            String documentId = DocumentsContract.getDocumentId(uri);

            if(TextUtils.equals(uri.getAuthority(),"com.android.providers.media.documents")){
                String id = documentId.split(":")[1];
                String selection = MediaStore.Images.Media._ID+"="+id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,selection);
            }else if(TextUtils.equals(uri.getAuthority(),"com.android.providers.downloads.documents")){
                if(documentId!=null && documentId.startsWith("msf:")){
                    resolveMSFContent(uri,documentId);
                    return;
                }
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),Long.valueOf(documentId));
                imagePath = getImagePath(contentUri,null);
            }
        }else if("content".equalsIgnoreCase(uri.getScheme())){
            imagePath = getImagePath(uri,null);
        }else if("file".equalsIgnoreCase(uri.getScheme())){
            imagePath = uri.getPath();
        }
        displayImage(imagePath);
    }

    private void resolveMSFContent(Uri uri, String documentId) {
        File file = new File(getActivity().getCacheDir(),"temp_file"+getActivity().getContentResolver().getType(uri).split("/"));
        //文件的读写操作
        try {
            InputStream inputStream = getActivity().getContentResolver().openInputStream(uri);
            OutputStream outputStream = new FileOutputStream(file);
            byte[] buffer = new byte[4*1024];
            int read;
            while((read = inputStream.read(buffer)) != -1){
                outputStream.write(buffer,0,read);
            } //循环结束之后，输入流的数据已经进入文件中
            outputStream.flush();

            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            mPicture.setImageBitmap(bitmap);
            imageBase64 = ImageUtil.imageToBase64(bitmap);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private String getImagePath(Uri uri, String selection){
        String path = null;
        Cursor cursor = getActivity().getContentResolver().query(uri,null,selection,null,null);
        if(cursor != null){
            if(cursor.moveToFirst()){
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();//关闭光标，否则会造成内存泄露
        }
        return path;
    }

    private void displayImage(String imagePath) throws FileNotFoundException {
        if(imagePath != null){
            boolean flag = getRootPath(mContext);
            FileInputStream fis = new FileInputStream(imagePath);
            Bitmap bitmap  = BitmapFactory.decodeStream(fis);
//            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            mPicture.setImageBitmap(bitmap);
            String imageToBase64 = ImageUtil.imageToBase64(bitmap);
            imageBase64 = imageToBase64;
        }
    }

    public void choosePhoto(View view) {
        if(ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
            //打开相册
            openAlbum();
        }else{
            //去申请权限
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},0);
        }
    }

    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent,REQUEST_CODE_CHOOSE);
    }

}


