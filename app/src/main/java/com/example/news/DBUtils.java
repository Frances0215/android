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
    private static String password = "070621";
    //private static String password = "";
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


}
