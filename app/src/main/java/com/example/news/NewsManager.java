package com.example.news;

import static com.example.news.DBUtils.getConn;

import android.content.Context;
import android.text.Editable;
import android.util.Log;

import com.example.news.ui.home.News;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NewsManager {
    private static final String TAG = "NewsManager";
    private static final String TABLE_NAME = "news";
    public static final String ID = "ID";
    public static final String TITLE = "title";
    public static final String PUBLISHER = "publisher";
    public static final String TYPE = "category";
    public static final String PUBLISH_TIME = "publish_time";
    public static final String CONTENT="contents";
    private static final String DB_NAME = "newsRec";
    private static DBUtils myDBUtil=new DBUtils();;
    private Context mContext = null;

    public NewsManager(Context context){
        mContext=context;
    }
    public static List<News> getNews() {
        List<News> myNews = new ArrayList<>();
        Connection connection = myDBUtil.getConn(DB_NAME);
        if (connection != null) {
            String sql = "select * from news";
            try {
                java.sql.Statement statement = connection.createStatement();
                ResultSet rSet = statement.executeQuery(sql);//得到数据库中的数据
                while (rSet.next()) {
                    //columnLabel是属性名
                    News myNew = new News(null, null, null, null, null, null);
                    myNew.setTitle(rSet.getString("title"));
                    //myNew.setPublisher(rSet.getString("publisher"));
                    myNew.setPublishTime(rSet.getString("publish_time"));
                    myNew.setID(rSet.getString("news_id"));
                    myNew.setContents(rSet.getString("contents"));
                    myNew.setType(rSet.getString("category"));
                    myNews.add(myNew);
                    Log.e(TAG, "数组组装成功");
                }
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        ;
        Log.e(TAG, "数组已返回");
        return myNews;

    }

    //根据新闻类型得到新闻,每次往后获取十条
    public static List<News> getNewsByType(String type, Integer start) {
        List<News> myNews = new ArrayList<>();
        Connection connection = myDBUtil.getConn(DB_NAME);
        String sql;
        if (connection != null) {
            int flag = start*10;
            sql = "select * from news where category = '" + type +"' order by news_id limit "+flag+",10";
            try {
                java.sql.Statement statement = connection.createStatement();
                ResultSet rSet = statement.executeQuery(sql);//得到数据库中的数据
                while (rSet.next()) {
                    //columnLabel是属性名
                    News myNew = new News(null, null, null, null, null, null);
                    myNew.setTitle(rSet.getString("title"));
                    //myNew.setPublisher(rSet.getString("publisher"));
                    myNew.setPublishTime(rSet.getString("publish_time"));
                    myNew.setID(rSet.getString("news_id"));
                    myNew.setContents(rSet.getString("contents"));
                    myNew.setType(rSet.getString("category"));
                    myNews.add(myNew);
                    Log.e(TAG, "数组组装成功");
                }
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        ;
        Log.e(TAG, "数组已返回");
        return myNews;

    }

    public int addNews(String content){
        int i=0;
        Connection connection = myDBUtil.getConn(DB_NAME);
        if (connection != null) {
            String sql = "UPDATE news SET contents = '"+content+"' where news_id=0001"  ;
            try {
                java.sql.Statement statement = connection.createStatement();
                i = statement.executeUpdate(sql);//得到数据库中的数据

                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        Log.e(TAG, "数组已返回");
        return i;
    }

    //得到推荐的新闻
    public static List<News> getRecByUserId(String ID, Integer start) {
        List<News> myNews = new ArrayList<>();
        List<String> recID = getRecID(ID, start);
        Connection connection = myDBUtil.getConn(DB_NAME);
        if (connection != null) {
            String id = "";
            for(int i=0;i<recID.size();i++){
                if(i==0)
                    id = id+"'"+recID.get(i)+"'";
                else
                    id = id+",'"+recID.get(i)+"'";
            }
            String sql = "select * from news where news_id in ("+id+")";
            try {
                java.sql.Statement statement = connection.createStatement();
                ResultSet rSet = statement.executeQuery(sql);//得到数据库中的数据

                while (rSet.next()) {
                    //columnLabel是属性名
                    News myNew = new News(null, null, null, null, null, null);
                    myNew.setTitle(rSet.getString("title"));
                    //myNew.setPublisher(rSet.getString("publisher"));
                    myNew.setPublishTime(rSet.getString("publish_time"));
                    myNew.setID(rSet.getString("news_id"));
                    myNew.setContents(rSet.getString("contents"));
                    myNew.setType(rSet.getString("category"));
                    myNews.add(myNew);
                    Log.e(TAG, "数组组装成功");
                }
//                if(rSet.getFetchSize()<flag){
//                    int b = flag - rSet.getFetchSize()+10;
//                    String  sql2 = "select * from news limit "+rSet.getFetchSize()+","+b;
//                    java.sql.Statement statement2 = connection.createStatement();
//                    ResultSet rSet2 = statement.executeQuery(sql2);//得到数据库中的数据
//
//                    while (rSet.next()) {
//                        //columnLabel是属性名
//                        News myNew = new News(null, null, null, null, null, null);
//                        myNew.setTitle(rSet.getString("title"));
//                        //myNew.setPublisher(rSet.getString("publisher"));
//                        myNew.setPublishTime(rSet.getString("publish_time"));
//                        myNew.setID(rSet.getString("news_id"));
//                        myNew.setContents(rSet.getString("contents"));
//                        myNew.setType(rSet.getString("category"));
//                        myNews.add(myNew);
//                        Log.e(TAG, "数组组装成功");
//                    }
//
//                }
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        Log.e(TAG, "数组已返回");
        return myNews;
    }

    //得到推荐的新闻
    public static List<String> getRecID(String ID, Integer start) {
        List<String> recID = new ArrayList<>();
        Connection connection = myDBUtil.getConn(DB_NAME);
        if (connection != null) {
            int flag = start*10;
            String  sql = "select * from itemcf_baseline where user_id = '" + ID +"'";
            try {
                java.sql.Statement statement = connection.createStatement();
                ResultSet rSet = statement.executeQuery(sql);//得到数据库中的数据
                while (rSet.next())
                    for (int i =1;i<=50;i++) {
                        //columnLabel是属性名
                        String index = "article_"+i;
                        recID.add(rSet.getString(index));
                        Log.e(TAG, "数组组装成功");
                    }
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        Log.e(TAG, "数组已返回");
        return recID;
    }

    public int newsClickEvent(String user_ID,String news_ID){
        Connection connection = myDBUtil.getConn(DB_NAME);
        int value=0;
        if (connection != null) {
            String sql = "insert into user_click(user_id,click_article_id) values(?,?);";
            try {
                PreparedStatement pstm = connection.prepareStatement(sql);
                //赋值
                pstm.setString(1,user_ID);
                pstm.setString(2,news_ID);
                value = pstm.executeUpdate();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        Log.e(TAG, "数组已返回");
        return value;
    }

    public void showClickEvent(){
        Connection connection = myDBUtil.getConn(DB_NAME);
        if (connection != null) {
            String sql = "select * from user_click";
            try {
                java.sql.Statement statement = connection.createStatement();
                ResultSet rSet = statement.executeQuery(sql);
                while (rSet.next()){
                    Log.e(TAG, rSet.getString("user_id"));
                    Log.e(TAG, rSet.getString("click_article_id"));
                }
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    public List<News> searchByWord(Editable keyWord){
        List<News> myNews = new ArrayList<>();
        Connection connection = myDBUtil.getConn(DB_NAME);
        String sql;
        if (connection != null) {
            sql = "select * from news where title likes '%"+keyWord+"%'";
            try {
                java.sql.Statement statement = connection.createStatement();
                ResultSet rSet = statement.executeQuery(sql);//得到数据库中的数据
                while (rSet.next()) {
                    //columnLabel是属性名
                    News myNew = new News(null, null, null, null, null, null);
                    myNew.setTitle(rSet.getString("title"));
                    myNew.setPublishTime(rSet.getString("publish_time"));
                    myNew.setID(rSet.getString("news_id"));
                    myNew.setContents(rSet.getString("contents"));
                    myNew.setType(rSet.getString("category"));
                    myNews.add(myNew);
                    Log.e(TAG, "数组组装成功");
                }
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        ;
        Log.e(TAG, "数组已返回");
        return myNews;
    }



}
