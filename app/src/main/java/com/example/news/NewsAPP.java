package com.example.news;

import android.app.Application;

import java.util.ArrayList;

//用于存放全局变量
public class NewsAPP extends Application {

    private UserData myUser;
    private String[] news_type = {"推荐","实时","政治","军事","娱乐","法律","经济","社会","科技","健康","时尚","美食"};
    private ArrayList<String> my_news_type = new ArrayList<String>();
    private static final int DB_VERSION = 2;
    private static final String DB_NAME = "Data2.dp";

    public ArrayList<String> getMy_news_type() {
        return my_news_type;
    }

    public String getPwd() {
        return myUser.getPassword();
    }

    public UserData getMyUser() {
        return myUser;
    }

    public static int getDbVersion() {
        return DB_VERSION;
    }

    public static String getDbName() {
        return DB_NAME;
    }

    public String getUserPhone() {
        return myUser.getPhone();
    }

    public String[] getNews_type() {
        return news_type;
    }

    public void setNews_type(String[] news_type) {
        this.news_type = news_type;
    }

    public void setUserPhone(String userPhone) {
        myUser.setPhone(userPhone);
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
