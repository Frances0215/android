package com.example.news.ui.photograph;

import static android.app.Activity.RESULT_OK;
import java.text.SimpleDateFormat;
import android.app.Activity;
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
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
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

//import com.baidu.ocr.sdk.OCR;
import com.baidu.ocr.sdk.OCR;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.AccessToken;
import com.example.news.R;
import com.example.news.ui.user.AppUsageActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
//import com.alibaba.fastjson.JSONObject;
//import com.chinamobile.cmss.sdk.ocr.ECloudDefaultClient;
//import com.chinamobile.cmss.sdk.ocr.http.constant.Region;
//import com.chinamobile.cmss.sdk.ocr.http.signature.Credential;
//import com.chinamobile.cmss.sdk.ocr.request.IECloudRequest;
//import com.chinamobile.cmss.sdk.ocr.request.ocr.OcrRequestFactory;
//import com.chinamobile.cmss.sdk.ocr.util.ImageUtil;

import java.io.File;
import java.util.HashMap;






    public class PhotographFragment extends Fragment  {

        Activity context;


        private static final int REQUEST_CODE_GENERAL_BASIC = 106;
        private static String UriPath;
        private boolean hasGotToken = false;

    private PhotographViewModel photographViewModel;

    private FrameLayout mFlCamera;

    private static final int REQUEST_CODE_TAKE = 1;
    private static final int REQUEST_CODE_CHOOSE = 0;
    private Uri imageUri; //??????????????????
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
                initAccessToken();

                mFlCamera=view.findViewById(R.id.mFlCamera);
                mFlCamera.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        takePhoto(view);
                    }
                });



    }



    public void takePhoto(View view) {
        if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED){
            //????????????
            if(hasGotToken){
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                UriPath=Environment.getExternalStorageDirectory().getPath();
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
                    Uri photoUri = FileProvider.getUriForFile(getActivity().getApplicationContext(), "com.example.news.fileprovider", file);
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
            //???????????????
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.CAMERA},1);
        }
    }


        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);

            Log.v("phtograph","enterFile");

            if (resultCode == Activity.RESULT_OK){
                String sdStatus = Environment.getExternalStorageState();

                if(!sdStatus.equals(Environment.MEDIA_MOUNTED)){
                    System.out.println(" ------------- sd card is not avaiable ---------------");
                    return;
                }


//                String name = "photo.jpg";
//                Bundle bundle = data.getExtras();
//                Bitmap bitmap = (Bitmap) bundle.get("data");
//                Log.v("bitmap",bitmap.toString());
//                File dir=new File(getActivity().getExternalFilesDir(null).getPath()+"/");
//                Log.v("",dir.getAbsolutePath());
//                if (!dir.exists()){
//                    dir.mkdir();
//                }
//                //????????????
//                File file = new File(dir+name);
//                if (!file.exists()){
//                    try {
//                        file.createNewFile();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }

//            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/");
//            file.mkdirs(); //???????????????
//            String fileName = Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+name;
                //String fileName = "sdcard"+"/"+name;
//                FileOutputStream fos =null;
                FileInputStream fis=null;
                RecognizeService.recAccurateBasic(getActivity().getApplicationContext(),UriPath,
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
                                    d = "?????????";
                                }

                                infoPopText(d);
                            }
                        });


//                try {
////                    System.out.println(fileName);
//                    Log.v("out",file.getAbsolutePath());
//                    fos = new FileOutputStream(file);
//                    bitmap.compress(Bitmap.CompressFormat.JPEG,100,fos);
//                    if (requestCode == REQUEST_CODE_GENERAL_BASIC && resultCode == Activity.RESULT_OK) {
//                        RecognizeService.recAccurateBasic(getActivity().getApplicationContext(),file.getAbsolutePath(),
//                                new RecognizeService.ServiceListener() {
//                                    @Override
//                                    public void onResult(String result) {
//                                        String d="";
//                                        String[] tempa=result.split("]");
//                                        String[] temp=tempa[0].split("\"words\":");
//                                        if(temp.length>1){
//
//                                            for(int i=1;i<temp.length;i++){
//                                                d=d+temp[i];
//                                            }
//                                            d=d.replace("\"","");
//                                            d=d.replace("{","");
//                                            d=d.replace(",","");
//                                        }
//                                        else{
//                                            d="?????????";
//                                        }
//
//                                    infoPopText(result);
//                                    }
//                                });
//                    }
//                } catch (FileNotFoundException e) {
//                    Log.v("NNOOOOOOOO","no photo");
//                    e.printStackTrace();
//                }finally {
//                    try {
//                        fos.flush();
//                        fos.close();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//
//                }
//                textGeneral(file.getAbsolutePath());
            }


        }
        private boolean checkTokenStatus() {
            if (!hasGotToken) {
                Toast.makeText(getActivity().getApplicationContext(), "token??????????????????", Toast.LENGTH_LONG).show();
            }
            return hasGotToken;
        }

        private void infoPopText(final String result) {
            Intent intent = new Intent(getActivity(), PhotoResultActivity.class);
            Bundle bundle=new Bundle();
            bundle.putString("result",result);
            intent.putExtras(bundle);
            startActivity(intent);
        }

        private void initAccessToken() {
            OCR.getInstance(getActivity().getApplicationContext()).initAccessToken(new OnResultListener<AccessToken>() {
                @Override
                public void onResult(AccessToken accessToken) {
                    String token = accessToken.getAccessToken();
                    hasGotToken = true;
                }

                @Override
                public void onError(OCRError error) {
                    error.printStackTrace();
                    Log.v("licence????????????token??????", error.getMessage());
                }
            }, getActivity().getApplicationContext());
        }

//        public static void textGeneral(String filepath) {
//            HashMap<String, Object> generalParams = new HashMap<>();
//            JSONObject generalOptions = new JSONObject();
//            generalOptions.put("rotate_180", true);
//            generalOptions.put("language", "zh");
//            generalParams.put("options", generalOptions);
//            String img_path =  filepath; //?????????????????????????????????????????????
//            //?????????????????????
//            IECloudRequest generalRequest = OcrRequestFactory.getOcrRequest("/api/ocr/v1/general", img_path, generalParams);
//
//            //??????????????????base64??????
//            IECloudRequest generalRequestBase64 = OcrRequestFactory.getOcrBase64Request("/api/ocr/v1/general",
//                    ImageUtil.fileToBase64(img_path),generalParams);
//
//            try {
//                JSONObject response = (JSONObject) client.call(generalRequest);
//                String content = response.toString();
//                System.out.println(content);
//                String output = "";
//                for(int i = 0; i + 13 < content.length(); i++) {
//                    if (content.charAt(i) == 'i' && content.charAt(i + 1) == 't' && content.charAt(i + 2) == 'e' && content.charAt(i + 3) == 'm' && content.charAt(i + 4) == 's' && content.charAt(i + 5) == 't' && content.charAt(i + 6) == 'r' && content.charAt(i + 7) == 'i' && content.charAt(i + 8) == 'n' && content.charAt(i + 9) == 'g') {
//                        i += 13;
//                        while (content.charAt(i) != '\"') {
//                            output += Character.toString(content.charAt(i));
//                            i++;
//                            //System.out.println(output);
//                        }
//                    }
//                }
//                JSONObject responseBase64 = (JSONObject) client.call(generalRequestBase64);
//                System.out.println(output);
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }


//
//    //???????????????????????????
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if(requestCode==1){
//            if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
//                doTake();
//            }else{
//                Toast.makeText(getActivity(),"??????????????????????????????",Toast.LENGTH_SHORT).show();
//            }
//        }else if(requestCode == 0){
//            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
//                openAlbum();
//            }else{
//                Toast.makeText(getActivity(),"?????????????????????????????????",Toast.LENGTH_SHORT).show();
//            }
//        }
//    }
//
//    private void doTake() {
//        File imageTemp = new File("imageOut.jpeg");
//        if(imageTemp.exists()){
//            imageTemp.delete();
//        }
//        try {
//            imageTemp.createNewFile();
//        }catch (IOException e){
//            e.printStackTrace();
//        }
//        imageUri = Uri.fromFile(imageTemp);
//        if(Build.VERSION.SDK_INT > 24){
//            //contentProvider
//            imageUri = FileProvider.getUriForFile(getActivity(),"com.example.ui.fileprovider",imageTemp);
//        }else{
//            imageUri = Uri.fromFile(imageTemp);
//        }
//        //??????????????????
//        Intent intent = new Intent();
//        intent.setAction("android.media.action.IMAGE_CAPTURE");
//        intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);      //????????????
//        startActivityForResult(intent,REQUEST_CODE_TAKE);
//    }
//
//
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if(requestCode==REQUEST_CODE_TAKE){
//            if(resultCode==RESULT_OK){
//                //?????????????????????
//                try {
//                    InputStream inputStream = getContentResolver().openInputStream(imageUri);
//                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
//                    mPicture.setImageBitmap(bitmap);//????????????
////                    //????????????
//                    String imageToBase64 = ImageUtil.imageToBase64(bitmap);
//                    imageBase64 = imageToBase64;
//
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                }
//            }
//        }else if(requestCode == REQUEST_CODE_CHOOSE){ //????????????????????????
//            if(Build.VERSION.SDK_INT < 19){
//                try {
//                    handleImageBeforeApi19(data);
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                }
//            }else{
//                try {
//                    handleImageOnApi19(data);
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                }
//            }
//
//        }
//    }
//
//    public static boolean getRootPath(Context context) {
//
//// ?????????SD???
//
//        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)
//
//                || !Environment.isExternalStorageRemovable()) {
//
//            //return context.getExternalCacheDir().getPath(); // ???
//            return true;
//
//        } else {
//
//            // return context.getCacheDir().getPath(); // ???
//            return false;
//        }
//
//    }
//
//    private void handleImageBeforeApi19(Intent data) throws FileNotFoundException {
//        Uri uri = data.getData();
//        String imagePath = getImagePath(uri,null);
//        displayImage(imagePath);
//    }
//
//    @TargetApi(19)
//    private void handleImageOnApi19(Intent data) throws FileNotFoundException { //??????uri??????????????????
//        String imagePath = null;
//        Uri uri = data.getData();
//        if(DocumentsContract.isDocumentUri(this,uri)){
//            String documentId = DocumentsContract.getDocumentId(uri);
//
//            if(TextUtils.equals(uri.getAuthority(),"com.android.providers.media.documents")){
//                String id = documentId.split(":")[1];
//                String selection = MediaStore.Images.Media._ID+"="+id;
//                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,selection);
//            }else if(TextUtils.equals(uri.getAuthority(),"com.android.providers.downloads.documents")){
//                if(documentId!=null && documentId.startsWith("msf:")){
//                    resolveMSFContent(uri,documentId);
//                    return;
//                }
//                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),Long.valueOf(documentId));
//                imagePath = getImagePath(contentUri,null);
//            }
//        }else if("content".equalsIgnoreCase(uri.getScheme())){
//            imagePath = getImagePath(uri,null);
//        }else if("file".equalsIgnoreCase(uri.getScheme())){
//            imagePath = uri.getPath();
//        }
//        displayImage(imagePath);
//    }
//
//    private void resolveMSFContent(Uri uri, String documentId) {
//        File file = new File(getCacheDir(),"temp_file"+getContentResolver().getType(uri).split("/"));
//        //?????????????????????
//        try {
//            InputStream inputStream = getContentResolver().openInputStream(uri);
//            OutputStream outputStream = new FileOutputStream(file);
//            byte[] buffer = new byte[4*1024];
//            int read;
//            while((read = inputStream.read(buffer)) != -1){
//                outputStream.write(buffer,0,read);
//            } //????????????????????????????????????????????????????????????
//            outputStream.flush();
//
//            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
//            mPicture.setImageBitmap(bitmap);
//            imageBase64 = ImageUtil.imageToBase64(bitmap);
//
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }
//
//    private String getImagePath(Uri uri, String selection){
//        String path = null;
//        Cursor cursor = getContentResolver().query(uri,null,selection,null,null);
//        if(cursor != null){
//            if(cursor.moveToFirst()){
//                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
//            }
//            cursor.close();//??????????????????????????????????????????
//        }
//        return path;
//    }
//
//    private void displayImage(String imagePath) throws FileNotFoundException {
//        if(imagePath != null){
//            boolean flag = getRootPath(mContext);
//            FileInputStream fis = new FileInputStream(imagePath);
//            Bitmap bitmap  = BitmapFactory.decodeStream(fis);
////            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
//            mPicture.setImageBitmap(bitmap);
//            String imageToBase64 = ImageUtil.imageToBase64(bitmap);
//            imageBase64 = imageToBase64;
//        }
//    }
//
//    public void choosePhoto(View view) {
//        if(ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
//            //????????????
//            openAlbum();
//        }else{
//            //???????????????
//            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},0);
//        }
//    }
//
//    private void openAlbum() {
//        Intent intent = new Intent("android.intent.action.GET_CONTENT");
//        intent.setType("image/*");
//        startActivityForResult(intent,REQUEST_CODE_CHOOSE);
//    }
//
//    public void back_to_mine(View view) {
//        finish();
//    }
}


