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
    public static final String USER_SEX = "sex";
    public static final String USER_BIRTHDAY = "birthday";
    private static final String DB_NAME = "newsRec";
    private Boolean isValid[] = new Boolean[5];
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
                String sql = "insert into users(birthday,name,pawd,sex,ID) values(?,?,?,?,?);";
                try {
                    PreparedStatement pstm = myConn.prepareStatement(sql);
                    //赋值
                    pstm.setString(1,userData.getBirthday());
                    pstm.setString(2,userData.getName());
                    pstm.setString(3,userData.getPassword());
                    pstm.setString(4,userData.getSex());
                    pstm.setString(5,userData.getID());//id和电话号码设置为相同

                    //执行更新数据库
                    int value = pstm.executeUpdate();
                    if (value > 0) {
                        Log.e("test", "register: 注册成功");
                    }else {
                        Log.e("test", "register: 注册失败");
                    }
                    myConn.close();
                    //closeDataBase();
                    pstm.close();


                } catch (java.sql.SQLException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

//    final  Handler mHandler = new Handler(){
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            switch (msg.what) {
//                case 0:
//                    //final boolean[] isValid = new boolean[1];
//                    isValid[0] = (Boolean)msg.obj;
//                    Log.e("isValid",""+isValid[0]);
//                    break;
//                default:
//                    break;
//            }
//        }
//
//    };

    //登录，查找用户密码和账户是否匹配
    public boolean isUserValid(String id,String psw){
        boolean flag = false;
        //boolean[] isValid = new boolean[1];
//        new Thread(new Runnable() {

           // @Override
            //public void run() {
                Connection myConn = myDBUtil.getConn(DB_NAME);
                String sql = "select * from users where ID = '"+ id+"'" + "and pawd = '" + psw +"'";
                try {
                    PreparedStatement pstm = myConn.prepareStatement(sql);
                    ResultSet rSet = pstm.executeQuery(sql);//得到数据库中的数据

                    if(rSet.next()) flag = true;
                    else flag=false;

//                    Message message = new Message();
//                    message.obj =flag;
//                    message.what = 0;
//                    mHandler.sendMessage(message);
                    //closeDataBase();
                    pstm.close();
                    myConn.close();
                } catch (java.sql.SQLException e) {
                    e.printStackTrace();
                }
                return flag;
//            }
//        }).start();
//
//        return isValid[0];
    }


    //根据id得到用户数据

    public UserData fetchUserData(String ID) throws SQLException {
        UserData user = new UserData(null,null,null,null,null,0,null,null,0,0,null);
//        new Thread(new Runnable() {
//
//            @Override
//            public void run() {
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


//            }
//        }).start();
        return user;
    }

    //得到所有用户的数据
    public ArrayList<UserData> fetchAllUserDatas() {
        ArrayList<UserData> users = new ArrayList<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Connection myConn = myDBUtil.getConn(DB_NAME);
                String sql = "select * from users";
                try {
                    PreparedStatement pstm = myConn.prepareStatement(sql);
                    ResultSet rSet = pstm.executeQuery(sql);//得到数据库中的数据
                    while (rSet.next()) {
                        UserData user = new UserData(null,null,null,null,null,0,null,null,0,0,null);

                        //columnLabel是属性名
                        user.setID(rSet.getString("ID"));
                        user.setName(rSet.getString("name"));
                        user.setPassword(rSet.getString("pawd"));
                        user.setSex(rSet.getString("sex"));
                        user.setBirthday(rSet.getString("birthday"));
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
                String sql ="delect from users where ID = '"+ID+"'";
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

    //根据id查找该用户是否存在
    public boolean findUserByPhone(String id){

        final boolean[] isValid = new boolean[1];
        Connection myConn = myDBUtil.getConn(DB_NAME);
        String sql = "select * from users where ID = '"+ id+"'";
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

        return isValid[0];
    }

    //更改用户密码
    public void updatePassword(String id,String newPwd){

        new Thread(new Runnable() {
            @Override
            public void run() {
                Connection myConn = myDBUtil.getConn(DB_NAME);
                String sql = "update users set pawd = '"+newPwd+"'"+"where ID = '"+ id +"'";
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
    public int updateUserData(String id,String birthday,String sex,String name,String type){
       // new Thread(new Runnable() {
//            @Override
//            public void run() {
                int result = 0;
                Connection myConn = myDBUtil.getConn(DB_NAME);
                String sql = "update users set birthday = '"+birthday+"',sex = '"+sex+"', name = '"+name+"',type = '"+type+"' where ID = '"+id+"'";
                try {
                    PreparedStatement pstm = myConn.prepareStatement(sql);
                    result = pstm.executeUpdate();//更新数据库中的数据
                    //closeDataBase();

                    pstm.close();
                    myConn.close();
                } catch (java.sql.SQLException e) {
                    e.printStackTrace();
                }

        return result;

    }
//        }).start();
//    }

    //更改用户数据，其中一项
    public void updateUserDataById(String content,String item,String id){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Connection myConn = myDBUtil.getConn(DB_NAME);
                String sql = "update users set " + item+"='"+content+"' where ID = '"+id+"'";
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
