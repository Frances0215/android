package com.example.news;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.entity.ContentType;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.impl.client.HttpClientBuilder;
import cz.msebera.android.httpclient.util.EntityUtils;

/**
 * 实时语音转写DEMO
 */
public class VoiceTrans {
    //TODO 使用移动云账户登录 https://ecloud.10086.cn/api/page/op-usercenter-static/#/aksk?productType=accesskey
    //得到用户的 accessKey 和 secret，然后替换下面对应变量
    private static  String accessKey = "c5215af31af34b4dbea82ac010f8cdfe";
    private static String secret =  "83f6ffb0f1c84604935acd23905c640e";
    private static String url = "/api/lingxiyun/cloud/ist/send_request/v1";
    private static String url2 = "/api/lingxiyun/cloud/ist/query_result/v1";
    private static String gatewayAddress = "https://api-wuxi-1.cmecloud.cn:8443";
    private static String iatHttpUrl;
    private static String sid;

    public static class IatReqVo {
        public Map<String, String> sessionParam;
        public String data;
        public Integer endFlag;
    }

    public static String sendFile(String filePath, Map<String, String> sessionParam, int sliceSize) throws Exception {
        File file = new File(filePath);
        if (!file.exists()) {
            return "文件不存在:" + file.getAbsolutePath();
        }

        String sid = UUID.randomUUID().toString();

        int sliceNum = (int) Math.ceil((double) file.length() / sliceSize);
        for (int i = 0; i < sliceNum; i++) {
            byte[] data = getData(file, i * sliceSize, sliceSize);
            if (data.length == 0) {
                break;
            }
            if (i > 0) {
                sessionParam = null;
            }

            send(String.valueOf(i+1), sessionParam, data, i == sliceNum - 1 ? 1 : 0);
        }
        return "调用完成";
    }
    public static void voiceTrans(String file) throws Exception {
        //TODO 替换为本地https证书文件全路径，证书文件通过运行 InstallCert 得到(先不管这个)
        //System.setProperty("javax.net.ssl.trustStore", "C:\\Users\\Lenovo\\Desktop\\demo2\\jssecacerts");
        sid = UUID.randomUUID().toString();
        ApiUrlTest urlTest = new ApiUrlTest();
        String urlpath = urlTest.doSignature(url, "POST", accessKey, secret);
        iatHttpUrl = gatewayAddress + urlpath;
        Map<String, String> sessionParam = new HashMap<>();
        sessionParam.put("sid", sid);
        sessionParam.put("aue", "raw");
        sessionParam.put("rate", "16k");
        sessionParam.put("rst", "json");
        sessionParam.put("bos", "30000");
        sessionParam.put("eos", "30000");
        sessionParam.put("language", "ch");
        sessionParam.put("dwa", "wpgs");
        sessionParam.put("pgs_mode", "simple");
        sessionParam.put("hotword", "住手");

        // TODO 替换为本地待转写的音频文件全路径（wav或pcm格式）
        //String file = "C:\\Users\\Lenovo\\Desktop\\16k_test.pcm";
        String msg = sendFile(file, sessionParam, 40960);
        System.out.println(msg);
        getResult();
    }



    public static void send(String number, Map<String, String> sessionParam, byte[] audioBytes, int endFlag) throws Exception {
        HttpPost httpPost = new HttpPost(iatHttpUrl);
        System.out.println(sid);
        httpPost.setHeader("streamId", sid);
        httpPost.setHeader("Content-Type", "application/json");
        httpPost.setHeader("number", number);
        httpPost.setHeader("language", "cn");
        httpPost.setHeader("user_id", "user_id0");

        // 设置HTTP数据
        String audioStr = Base64.getEncoder().encodeToString(audioBytes);
        IatReqVo vo = new IatReqVo();
        vo.data = audioStr;
        vo.endFlag = endFlag;
        vo.sessionParam = sessionParam;

        httpPost.setEntity(new StringEntity(JSON.toJSONString(vo), ContentType.APPLICATION_JSON));
        //httpPost.setEntity(new StringEntity(JSON.toJSONString(vo), HTTP.UTF_8));
        //发送HTTP请求
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpResponse httpResponse = httpClient.execute(httpPost);
        String response = EntityUtils.toString(httpResponse.getEntity());
        JSONObject jsonObject = JSONObject.parseObject(response);
        if(jsonObject != null) {
            if (jsonObject.get("body") == null
                    || jsonObject.getJSONArray("body").size() > 0) {
                System.out.println(response);
            }
        } else {
            System.out.println("no response.");
        }
    }
    public static byte[] getData(File file, int from, int size) throws Exception {
        InputStream in = null;
        try {
            in = new FileInputStream(file);
            byte[] tempbytes = new byte[Math.min(in.available(), size)];
            in = new FileInputStream(file);
            in.skip(from);
            in.read(tempbytes);
            return tempbytes;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e1) {
                    // ignore
                }
            }
        }
    }
    public static void getResult() throws Exception {
        ApiUrlTest urlTest = new ApiUrlTest();
        String urlpath = urlTest.doSignature(url2, "GET", accessKey, secret);
        iatHttpUrl = gatewayAddress + urlpath;
        HttpGet httpGet = new HttpGet(iatHttpUrl);
        httpGet.setHeader("streamId", sid);
        httpGet.setHeader("Content-Type", "application/json");
        //发送HTTP请求
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpResponse httpResponse = httpClient.execute(httpGet);
        String response = EntityUtils.toString(httpResponse.getEntity());
        JSONObject jsonObject = JSONObject.parseObject(response);
        if(jsonObject != null) {
            System.out.println("转写结果：" + jsonObject);
            String content = jsonObject.toString();
            //System.out.println(content);
            String output = "";
            for(int i = 0; i + 13 < content.length(); i++) {
                if (content.charAt(i) == '[' && content.charAt(i + 1) == '{' && content.charAt(i + 2) == '\\' && content.charAt(i + 3) == '\"' && content.charAt(i + 4) == 'w' && content.charAt(i + 5) == '\\' && content.charAt(i + 6) == '\"' && content.charAt(i + 7) == ':' && content.charAt(i + 8) == '\\' && content.charAt(i + 9) == '\"') {
                    i += 10;
                    while (content.charAt(i) != '\\') {
                        output += Character.toString(content.charAt(i));
                        i++;
                        System.out.println(output);
                    }
                }
            }
            System.out.println(output);
        } else {
            System.out.println("无转写结果");
        }
    }
}
