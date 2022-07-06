package com.chinamobile.cmss;

import com.alibaba.fastjson.JSONObject;
import com.chinamobile.cmss.sdk.face.ECloudDefaultClient;
import com.chinamobile.cmss.sdk.face.http.constant.Region;
import com.chinamobile.cmss.sdk.face.http.signature.Credential;
import com.chinamobile.cmss.sdk.face.request.IECloudRequest;
import com.chinamobile.cmss.sdk.face.request.face.FaceRequestFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

/**
 * @author CMSS
 * 测试人脸接口
 */
public class FaceDemo {
    public static String user_ak;
    private static String user_sk;
    private static ECloudDefaultClient client;

    static {

        user_ak = "c5215af31af34b4dbea82ac010f8cdfe";
        user_sk = "83f6ffb0f1c84604935acd23905c640e";

        Credential credential = new Credential(user_ak, user_sk);
        client = new ECloudDefaultClient(credential, Region.POOL_SZ);
    }

    public static void main(String[] args) {
  //        testFaceDetect();  //返回一个人脸的数字化信息
 //       testGetFaceStoreList(); //没用，我已经创好了人脸库了
//        creatfacestore();  //也没用，我已经创好了人脸库了
//          testCreateFace();  //往一个人脸库里加入一个人脸信息，库不用管，我嵌进去了
          testfacesearch(); //当前有一个人脸，在一个人脸库里面查找和这个人脸最像的那个人的用户ID和置信度（当前程度下，可以认为找出来最像的就是老顾客，你们写的时候，新用户注册就调用testCreateFace()，已经是用户登录的时候就用这个）
    }

    private static void creatfacestore() {
        JSONObject params = new JSONObject();
        params.put("name", "xyqwtest");
        params.put("description", "facestore of xyqw");
        IECloudRequest request = FaceRequestFactory.getFaceRequest("/api/human/face/v1/store/create", params);
        try{
            JSONObject response =(JSONObject)client.call(request);
            System.out.println(response.toString());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private static void testfacesearch() {
        List<String> s = new ArrayList<String>();
        s.add("62c51f0b68d65800016e8e46");
        JSONObject params = new JSONObject();
        params.put("image", imageFileToBase64("C:\\Users\\Lenovo\\Desktop\\IMG_20220528_121604_edit_448663128999784.jpg"));//改为自己的人脸图片路径
        params.put("imageType", "BASE64");
        params.put("faceStoreIds", s);
        params.put("maxFaceNum", 1);
        IECloudRequest request = FaceRequestFactory.getFaceRequest("/api/human/face/v1/search", params);
        try{
            JSONObject response =(JSONObject)client.call(request);
            String content = response.toString();
            //System.out.println(response.toString());

            String output = "";
            for(int i = 0; i + 13 < content.length(); i++) {
                if (content.charAt(i) == 'm' && content.charAt(i + 1) == 'e' && content.charAt(i + 2) == 'm' && content.charAt(i + 3) == 'b' && content.charAt(i + 4) == 'e' && content.charAt(i + 5) == 'r' && content.charAt(i + 6) == 'I' && content.charAt(i + 7) == 'd') {
                    i += 11;
                    while (content.charAt(i) != '\"') {
                        output += Character.toString(content.charAt(i));
                        i++;
                        //System.out.println(output);
                    }
                }
            }
            System.out.println(output);
            output = "";
            for(int i = 0; i + 13 < content.length(); i++) {
                if (content.charAt(i) == 'c' && content.charAt(i + 1) == 'o' && content.charAt(i + 2) == 'n' && content.charAt(i + 3) == 'f' && content.charAt(i + 4) == 'i' && content.charAt(i + 5) == 'd' && content.charAt(i + 6) == 'e' && content.charAt(i + 7) == 'n' && content.charAt(i + 8) == 'c'&& content.charAt(i + 9) == 'e') {
                    i += 12;
                    while (content.charAt(i) != ',') {
                        output += Character.toString(content.charAt(i));
                        i++;
                        //System.out.println(output);
                    }
                }
            }
            System.out.println(output);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private static String imageFileToBase64(String imageFilePath) {
        InputStream in = null;
        byte[] data = null;

        try {
            in = new FileInputStream(imageFilePath);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException var4) {
            var4.printStackTrace();
        }

        return Base64.getEncoder().encodeToString(data);
    }


    public static  void testCreateFace(){
        JSONObject params = new JSONObject();
        params.put("faceStoreId", "62c51f0b68d65800016e8e46"); //我嵌入的人脸库
        params.put("name", "蔡奇"); //后面那项随便改，名字
        params.put("image", imageFileToBase64("C:\\Users\\Lenovo\\Desktop\\IMG_20220528_121604_edit_448663128999784.jpg"));//改为自己的人脸图片路径
        IECloudRequest request = FaceRequestFactory.getFaceRequest("/api/human/face/v1/store/member/create", params);
        try{
            JSONObject response =(JSONObject)client.call(request);
            System.out.println(response.getString("state"));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void testFaceDetect(){
        JSONObject params = new JSONObject();
        params.put("image",imageFileToBase64("C:\\Users\\Lenovo\\Desktop\\IMG_20220528_121604_edit_448663128999784.jpg"));//改为自己的人脸图片路径
        params.put("imageType","BASE64");
        params.put("maxFaceNum","1");
        IECloudRequest faceDetectRequest = FaceRequestFactory.getFaceRequest("/api/human/face/v1/detect", params);
        try{
            JSONObject response = (JSONObject) client.call(faceDetectRequest);
            System.out.println(response.toString());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void testGetFaceStoreList(){
        JSONObject params = new JSONObject();
        params.put("offset", 1);
        params.put("limit",10);
        IECloudRequest faceStoreListRequest = FaceRequestFactory.getFaceRequest("/api/human/face/v1/store/list", params);
        try{
            JSONObject response = (JSONObject) client.call(faceStoreListRequest);
            System.out.println(response.toString());
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}