package com.example.news;

import android.app.Application;

import java.util.ArrayList;

//用于存放全局变量
public class NewsAPP extends Application {

    private UserData myUser = new UserData(null,null,null,null,null,0,null,null,0,0,null);
    private String[] news_type = {"财经","彩票","房产","股票","家居","教育","科技","社会","时尚","时政","体育","星座","游戏","娱乐"};
    private ArrayList<String> my_news_type = new ArrayList<String>();
    private static final int DB_VERSION = 2;
    private static final String DB_NAME = "Data2.dp";

    public ArrayList<String> getMy_news_type() {
        return my_news_type;
    }

    public String getPwd() {
        return myUser.getPassword();
    }

    public String getID(){return myUser.getID();}
    public UserData getMyUser() {
        return myUser;
    }

    public static int getDbVersion() {
        return DB_VERSION;
    }

    public static String getDbName() {
        return DB_NAME;
    }

    public String[] getNews_type() {
        return news_type;
    }

    public void setNews_type(String[] news_type) {
        this.news_type = news_type;
    }

    public void setUserID(String id) {
        myUser.setID(id);
    }

    public void setMy_news_type(ArrayList<String> my_news_type) {
        this.my_news_type = my_news_type;
    }

    public void setPwd(String pwd) {
        myUser.setPassword(pwd);
    }
    public void setName(String name){myUser.setName(name);}
    public void setMyUser(UserData myUser) {
        this.myUser = myUser;
    }

    //清空user
    public void clearUser(){
        myUser.clear();
    }
}
