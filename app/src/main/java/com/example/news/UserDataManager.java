package com.example.news;

import android.content.ContentValues;
import android.content.Context;
import android.database.AbstractWindowedCursor;
import android.database.Cursor;
import android.database.CursorWindow;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.example.news.ui.home.News;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Objects;

//连接的是MySQL
public class UserDataManager {
    private static final String TAG = "UserDataManager";
    private static final String TABLE_NAME = "users";
    public static final String ID = "ID";
    public static final String USER_NAME = "name";
    public static final String USER_PWD = "pawd";
    public static final String USER_TELEPHONE = "phone";
    public static final String USER_SEX = "sex";
    public static final String USER_BIRTHDAY = "birthday";
    private static final String DB_NAME = "newsRec";
    private Context mContext = null;

    private DBUtils myDBUtil = new DBUtils();
 //   private Connection myConn = null;
//    private MyDatabaseHelper mDatabaseHelper;
//    private SQLiteDatabase mSQLiteDatabase;

    public UserDataManager(Context context) {
        mContext = context;
        Log.i(TAG,"UserDataManager construction!");
    }
//    //建立连接
//    public void openDataBase() {
//        myConn = myDBUtil.getConn(DB_NAME);
//    }
//    //关闭连接
//    public void closeDataBase() {
//        try {
//            myConn.close();
//        }catch (java.sql.SQLException e) {
//            e.printStackTrace();
//        }
//
//    }

    //添加新用户，即注册
    public void insertUserData(UserData userData) {

        new Thread(new Runnable() {

            @Override
            public void run() {
                Connection myConn = myDBUtil.getConn(DB_NAME);
                String sql = "insert into users(birthday,name,pawd,sex,phone,id) values(?,?,?,?,?,?)";
                try {
                    PreparedStatement pstm = myConn.prepareStatement(sql);
                    //赋值
                    pstm.setString(1,userData.getBirthday());
                    pstm.setString(2,userData.getName());
                    pstm.setString(3,userData.getPassword());
                    pstm.setString(4,userData.getSex());
                    pstm.setString(5,userData.getPhone());
                    pstm.setString(6,userData.getPhone());//id和电话号码设置为相同

                    //执行更新数据库
                    pstm.executeUpdate();
                    //closeDataBase();
                    pstm.close();

                    myConn.close();
                } catch (java.sql.SQLException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    //登录，查找用户密码和账户是否匹配
    public boolean isUserValid(String phone,String psw){
        final boolean[] isValid = new boolean[1];
        new Thread(new Runnable() {

            @Override
            public void run() {
                Connection myConn = myDBUtil.getConn(DB_NAME);
                String sql = "select * from users where phone = '"+ phone+"'" + "and pawd = '" + psw +"'";
                try {
                    PreparedStatement pstm = myConn.prepareStatement(sql);
                    ResultSet rSet = pstm.executeQuery(sql);//得到数据库中的数据
                    if(rSet.next()) isValid[0] = true;
                    else isValid[0]=false;

                    //closeDataBase();
                    pstm.close();
                    myConn.close();
                } catch (java.sql.SQLException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        return isValid[0];
    }


    //根据id得到用户数据

    public UserData fetchUserData(String ID) throws SQLException {
        UserData user = new UserData(null,null,null,null,null,null,0,null,null,0,0,null);
        new Thread(new Runnable() {

            @Override
            public void run() {
                Connection myConn = myDBUtil.getConn(DB_NAME);

                String sql = "select * from users where ID = '"+ ID+"'";
                try {
                    PreparedStatement pstm = myConn.prepareStatement(sql);
                    ResultSet rSet = pstm.executeQuery(sql);//得到数据库中的数据
                    while (rSet.next()) {
                        //columnLabel是属性名
                        user.setID(rSet.getString("ID"));
                        user.setName(rSet.getString("name"));
                        user.setPassword(rSet.getString("pawd"));
                        user.setSex(rSet.getString("sex"));
                        user.setBirthday(rSet.getString("birthday"));
                        user.setPhone(rSet.getString("phone"));
                        user.setAge(rSet.getInt("age"));
                        user.setUserSystem(rSet.getString("userSystem"));
                        user.setArea(rSet.getString("area"));
                        user.setEnvironment(rSet.getInt("environment"));
                        user.setEquipment(rSet.getInt("equipment"));
                        user.setType(rSet.getString("type"));
                    }

                    //closeDataBase();
                    pstm.close();
                    myConn.close();
                } catch (java.sql.SQLException e) {
                    e.printStackTrace();
                }


            }
        }).start();
        return user;
    }

    //得到所有用户的数据
    public ArrayList<UserData> fetchAllUserDatas() {
        ArrayList<UserData> users = new ArrayList<>();
        new Thread(new Runnable() {
            Connection myConn = myDBUtil.getConn(DB_NAME);
            @Override
            public void run() {
                String sql = "select * from users";
                try {
                    PreparedStatement pstm = myConn.prepareStatement(sql);
                    ResultSet rSet = pstm.executeQuery(sql);//得到数据库中的数据
                    while (rSet.next()) {
                        UserData user = new UserData(null,null,null,null,null,null,0,null,null,0,0,null);

                        //columnLabel是属性名
                        user.setID(rSet.getString("ID"));
                        user.setName(rSet.getString("name"));
                        user.setPassword(rSet.getString("pawd"));
                        user.setSex(rSet.getString("sex"));
                        user.setBirthday(rSet.getString("birthday"));
                        user.setPhone(rSet.getString("phone"));
                        user.setAge(rSet.getInt("age"));
                        user.setUserSystem(rSet.getString("userSystem"));
                        user.setArea(rSet.getString("area"));
                        user.setEnvironment(rSet.getInt("environment"));
                        user.setEquipment(rSet.getInt("equipment"));
                        user.setType(rSet.getString("type"));
                        users.add(user);
                    }

                    //closeDataBase();
                    pstm.close();
                    myConn.close();
                } catch (java.sql.SQLException e) {
                    e.printStackTrace();
                }


            }
        }).start();
        return users;
    }

    //根据id删除用户
    public void deleteUserData(String ID) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Connection myConn = myDBUtil.getConn(DB_NAME);
                String sql ="delect from users where id = '"+ID+"'";
                try {
                    PreparedStatement pstm = myConn.prepareStatement(sql);
                    pstm.executeUpdate();//更新数据库中的数据
                    //closeDataBase();
                    pstm.close();
                    myConn.close();
                } catch (java.sql.SQLException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    //根据电话号码查找该用户是否存在
    public boolean findUserByPhone(String phone){
        final boolean[] isValid = new boolean[1];
        new Thread(new Runnable() {
            Connection myConn = myDBUtil.getConn(DB_NAME);
            @Override
            public void run() {
                String sql = "select * from users where phone = '"+ phone+"'";
                try {
                    PreparedStatement pstm = myConn.prepareStatement(sql);
                    ResultSet rSet = pstm.executeQuery(sql);//得到数据库中的数据
                    if(rSet.next()) isValid[0] = true;
                    else isValid[0]=false;

                    //closeDataBase();
                    pstm.close();
                    myConn.close();
                } catch (java.sql.SQLException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        return isValid[0];
    }

    //更改用户密码
    public void updatePassword(String phone,String newPwd){

        new Thread(new Runnable() {
            @Override
            public void run() {
                Connection myConn = myDBUtil.getConn(DB_NAME);
                String sql = "update users set password = '"+newPwd+"'"+"where phone = '"+ phone +"'";
                try {
                    PreparedStatement pstm = myConn.prepareStatement(sql);
                    pstm.executeUpdate();//更新数据库中的数据
                    //closeDataBase();
                    pstm.close();
                    myConn.close();
                } catch (java.sql.SQLException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    //更改用户数据，根据电话号码
    public void updateUserData(String phone,String birthday,String sex,String name,String type){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Connection myConn = myDBUtil.getConn(DB_NAME);
                String sql = "update users set birthday = '"+birthday+"',sex = '"+sex+"', name = '"+name+"',type = '"+type+"' where id = '"+phone+"'";
                try {
                    PreparedStatement pstm = myConn.prepareStatement(sql);
                    pstm.executeUpdate();//更新数据库中的数据
                    //closeDataBase();
                    pstm.close();
                    myConn.close();
                } catch (java.sql.SQLException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    //更改用户数据，其中一项
    public void updateUserDataById(String content,String item,String id){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Connection myConn = myDBUtil.getConn(DB_NAME);
                String sql = "update users set " + item+"='"+content+"' where id = '"+id+"'";
                try {
                    PreparedStatement pstm = myConn.prepareStatement(sql);
                    pstm.executeUpdate();//更新数据库中的数据
                    //closeDataBase();
                    pstm.close();
                    myConn.close();
                } catch (java.sql.SQLException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
