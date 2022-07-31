package com.example.news.ui.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Editable;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.news.LoginActivity;
import com.example.news.NewsAPP;
import com.example.news.NewsManager;
import com.example.news.R;

import java.util.ArrayList;
import java.util.List;

public class SearchResultActivity extends AppCompatActivity {
    private ListView mLvSearch;
    private MyNewsListAdapter newsAdapter;
    private NewsManager mNewsManager;
    private List<News> myNews = new ArrayList<>();
    private String keyWord;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        Intent intent=this.getIntent();
        Bundle bundle = intent.getExtras();
        keyWord = (String) bundle.getSerializable("keyWord");

        mLvSearch = (ListView) findViewById(R.id.mLvNews);

        //初始化数据库
        if (mNewsManager == null) {
            mNewsManager = new NewsManager(this);
        }

        initNews(0);

        ClickThread myThread = new ClickThread();
        myThread.start();

        mLvSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                News news_c = (News) parent.getItemAtPosition(position);

                Message msg = Message.obtain();
                msg.obj = news_c;
                myThread.handler.sendMessage(msg);

                //跳转页面，并传递参数
                Bundle bundle = new Bundle();
                bundle.putSerializable("news",news_c);
                Intent intent = new Intent(SearchResultActivity.this, Show_news_Activity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        //用于添加上方标题栏中的返回按钮
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeActionContentDescription("点击返回新闻主页面");
        }

    }
    //返回上一个界面
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish(); // back button
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    class ClickThread extends Thread{
        public Handler handler;
        @Override
        public void run() {
            super.run();
            Looper.prepare();
            handler = new Handler() {
                @Override
                public void handleMessage(@NonNull Message msg) {
                    super.handleMessage(msg);
                    //Toast.makeText(SecondActivity.this, msg.obj.toString(), Toast.LENGTH_LONG).show();
                    News n = (News) msg.obj;
                    String news_id=n.getID();
                    NewsAPP mApp = (NewsAPP)getApplication();
                    String user_id = mApp.getID();
                    //Log.e("用户id",user_id);
                    mNewsManager.newsClickEvent(user_id,news_id);
                }
            };
            Looper.loop();

        }
    }

    final Handler mHandler = new Handler(){
        @SuppressLint("HandlerLeak")
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case 0:
                    List<News> news_temp;
                    news_temp=(List<News>)msg.obj;
                    myNews.addAll(news_temp);
                    if(news_temp.size()==0){
                        Toast.makeText(SearchResultActivity.this, "抱歉没有找到你需要的新闻",Toast.LENGTH_SHORT).show();
                        finish();
                    }else {
                        //设置新闻滑动页
                        newsAdapter = new MyNewsListAdapter(SearchResultActivity.this,R.layout.fragment_news_item,myNews);
                        mLvSearch.setAdapter(newsAdapter);
                    }
                    break;
                default:
                    break;
            }
        }

    };


    public void initNews(int flag){
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<News> mNews = new ArrayList<>();
                mNews = mNewsManager.searchByWord(keyWord);
                Message message = new Message();
                message.obj =mNews;
                message.what = flag;
                mHandler.sendMessage(message);
            }
        }).start();


    }

}
