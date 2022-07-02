package com.example.news;


import android.util.Log;

import com.example.news.ui.home.News;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//连接mySql数据库方法
public class DBUtils {
    private static final String TAG = "DBUtils";
    private static String driver = "com.mysql.jdbc.Driver";

    //这里是MySQL的用户名
    private static String user = "root";
    //这里是MySQL密码
//    private static String password = "070621";
    private static String password = "070621";
    public static Connection getConn(String dbName) {
        Connection connection = null;
        try {
            Class.forName(driver);

            //数据的IP地址，你的真实IP地址。应该是数据库的ip地址
            String ip = "114.132.126.248";
            //String ip="10.135.1.154";
            String port = "3306";//不用改
            String url = "jdbc:mysql://" + ip + ":" + port + "/" + dbName;
            connection = DriverManager.getConnection(url, user, password);
            Log.e("数据库连接", "成功!");
        } catch (Exception e) {
            Log.e("数据库连接", "失败!");
            e.printStackTrace();
        }
        return connection;
    }

//    public static List<News> getNews() {
//        List<News> myNews = new ArrayList<>();
//        Connection connection = getConn("newsRec");
//        if (connection != null) {
//            String sql = "select * from news";
//            try {
//                java.sql.Statement statement = connection.createStatement();
//                ResultSet rSet = statement.executeQuery(sql);//得到数据库中的数据
//                while (rSet.next()) {
//                    //columnLabel是属性名
//                    News myNew = new News(null, null, null, null, null, null);
//                    myNew.setTitle(rSet.getString("title"));
//                    myNew.setPublisher(rSet.getString("publisher"));
//                    myNew.setPublishTime(rSet.getString("publishTime"));
//                    myNew.setID(rSet.getString("ID"));
//                    myNew.setContents(rSet.getString("contents"));
//                    myNews.add(myNew);
//                    Log.e(TAG, "数组组装成功");
//                }
//                connection.close();
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        }
//        ;
//        Log.e(TAG, "数组已返回");
//        return myNews;
//
//    }
//
//    //根据新闻类型得到新闻
//    public static List<News> getNewsByType(String type) {
//        List<News> myNews = new ArrayList<>();
//        Connection connection = getConn("news");
//        if (connection != null) {
//            String sql = "select * from news where type = " + type ;
//            try {
//                java.sql.Statement statement = connection.createStatement();
//                ResultSet rSet = statement.executeQuery(sql);//得到数据库中的数据
//                while (rSet.next()) {
//                    //columnLabel是属性名
//                    News myNew = new News(null, null, null, null, null, null);
//                    myNew.setTitle(rSet.getString("title"));
//                    myNew.setPublisher(rSet.getString("publisher"));
//                    myNew.setPublishTime(rSet.getString("publishTime"));
//                    myNew.setID(rSet.getString("ID"));
//                    myNew.setContents(rSet.getString("contents"));
//                    myNews.add(myNew);
//                    Log.e(TAG, "数组组装成功");
//                }
//                connection.close();
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        }
//        ;
//        Log.e(TAG, "数组已返回");
//        return myNews;
//
//    }
//
//    public int addNews(String content){
//        int i=0;
//        Connection connection = getConn("newsRec");
//        if (connection != null) {
//            String sql = "UPDATE news SET content = '"+content+"' where ID=0001"  ;
//            try {
//                java.sql.Statement statement = connection.createStatement();
//                i = statement.executeUpdate(sql);//得到数据库中的数据
//
//                connection.close();
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        }
//        ;
//        Log.e(TAG, "数组已返回");
//        return i;
//    }
}


    //不实际使用，作为一个样本
//    public static String[] getEthData() {
//        String rsdata[]=new String[24];//用来存储数据
//        //dbName是数据库名
//        Connection connection = getConn("eth");
//        if (connection!=null){
//            //直接执行sql语句
//            String sql="select * from apidate order by id DESC limit 1";
//            try{
//                java.sql.Statement statement = connection.createStatement();
//                ResultSet rSet = statement.executeQuery(sql);//得到数据库中的数据
//                while (rSet.next()) {
//                    //columnLabel是属性名
//                    rsdata[0]=rSet.getString("date");//日期
//                    rsdata[1]=rSet.getString("time");//时间
//                    rsdata[2]=rSet.getString("capital").substring(0,4);//人民币总收益
//                    rsdata[3]=rSet.getString("worker_length");//矿机总数
//                    rsdata[4]=rSet.getString("worker_lenght_online");//在线矿机数
//                    rsdata[5]=rSet.getString("worker_length_offline");//离线矿机数
//                    rsdata[6]=rSet.getString("dead_sl");//失线矿机数
//                    rsdata[7]=rSet.getString("hash_24_hour");//24小时平均算力
//                    rsdata[8]=rSet.getString("last_day_value").substring(0,9);//过去24小时收益
//                    rsdata[9]=rSet.getString("hash_15");//15分钟平均算力
//                    rsdata[10]=rSet.getString("balance").substring(0,9);//当前余额
//                    rsdata[11]=rSet.getString("local_hash");//本地算力
//                    rsdata[12]=rSet.getString("value").substring(0,9);//总收益
//                    rsdata[13]=rSet.getString("quanwangsl")+" TH/s";//全网算力
//                    rsdata[14]=rSet.getString("f2poolsl")+" TH/s";//全网算力
//                    rsdata[15]=rSet.getString(
//                            "meiM_Eth")+" ETH ≈ ¥"
//                            +String.valueOf(Double.valueOf(rSet.getString("meiM_Eth"))
//                            *Double.valueOf(rSet.getString("meiyuanzhisu"))
//                            *Double.valueOf(rSet.getString("usdCnyRate")))
//                            .substring(0,5);//每M收益
//                    rsdata[16]= String.valueOf(
//                            Double.valueOf(rSet.getString("waikuangnandu"))/10000/10000/10000/1000)
//                            .substring(0,5)+" P";//当前难度
//                    rsdata[17]="$"+rSet.getString(
//                            "price")+" ≈  ¥"
//                            +String.valueOf(Double.valueOf(rSet.getString("price"))
//                            *Double.valueOf(rSet.getString("usdCnyRate")))
//                            .substring(0,4);//币价usdCnyRate
//                    rsdata[18]="¥"+rSet.getString("ETC_index");//etc指数
//                    rsdata[19]=rSet.getString("ETC_zhangdie");//etc涨跌
//                    rsdata[20]=rSet.getString("ETH_zuigaojia");//eth最高价
//                    rsdata[21]=rSet.getString("ETH_zuidijia");//eth最低价
//                    rsdata[22]=rSet.getString("riseandfall");//ETH涨跌
//                    rsdata[23]=rSet.getString("id");//数据库数据条数
//                    Log.e(TAG,"数组组装成功");
//                }
//                connection.close();
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        };
//        Log.e(TAG,"数组已返回");
//        return rsdata;
//    }
//    public static String getVersion() {
//        String version=null;
//        Connection connection = getConn("eth");
//        if (connection!=null){
//            String sql="select version from version";
//            try{
//                java.sql.Statement statement = connection.createStatement();
//                ResultSet rSet = statement.executeQuery(sql);
//                while (rSet.next()) {
//                    version=rSet.getString("version");//日期
//                    Log.e(TAG,"数组组装成功==="+version);
//                }
//                connection.close();
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        };
//        Log.e(TAG,"数组已返回==========");
//        return version;
//    }
//}

//public class DBUtils {
//    private static final String TAG = "DBUtils";
//    private static Connection getConnection(String dbName) {
//        Connection conn = null;
//        try {
//            Class.forName("com.mysql.jdbc.Driver"); //加载驱动
//            String ip = "139.199.38.177";//ip地址
//            conn = DriverManager.getConnection(
//                    "jdbc:mysql://" + ip + ":3306/" + dbName,
//                    "learner", "learner_password");
//        } catch (SQLException ex) {
//            ex.printStackTrace();
//        } catch (ClassNotFoundException ex) {
//            ex.printStackTrace();
//        }
//        return conn;
//    }
//    public static HashMap<String, String> getUserInfoByName(String name) {
//        HashMap<String, String> map = new HashMap<>();
//        Connection conn = getConnection("dblearner");
//        try {
//            Statement st = conn.createStatement();
//            String sql = "select * from user where name = '" + name + "'";
//            ResultSet res = st.executeQuery(sql);
//            if (res == null) {
//                return null;
//            } else {
//                int cnt = res.getMetaData().getColumnCount();
//                //res.last(); int rowCnt = res.getRow(); res.first();
//                res.next();
//                for (int i = 1; i <= cnt; ++i) {
//                    String field = res.getMetaData().getColumnName(i);
//                    map.put(field, res.getString(field));
//                }
//                conn.close();
//                st.close();
//                res.close();
//                return map;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            Log.d(TAG, " 数据操作异常");
//            return null;
//        }
//    }
//}