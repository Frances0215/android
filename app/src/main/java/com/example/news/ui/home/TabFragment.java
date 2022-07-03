package com.example.news.ui.home;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.news.DBUtils;
import com.example.news.NewsAPP;
import com.example.news.NewsManager;
import com.example.news.R;
import com.example.news.UserData;
import com.example.news.UserDataManager;

import java.util.ArrayList;
import java.util.List;

//滑动页fragment
public class TabFragment extends Fragment {

    private ListView mLvNews;
    private List<News> myNews = new ArrayList<>();
    private NewsManager mNewsManager;

    private Handler handler;
    private Looper myLooper;

    public static TabFragment newInstance(String label) {
        Bundle args = new Bundle();
        args.putString("label", label);
        TabFragment fragment = new TabFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tab, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //初始化数据库
        if (mNewsManager == null) {
            mNewsManager = new NewsManager(getActivity());
        }

        mLvNews = getView().findViewById(R.id.mLvNews);
        //新闻类别
        String label = getArguments().getString("label");
        initNews(label);

        mLvNews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                News news_c = (News) parent.getItemAtPosition(position);
                handler.obtainMessage(0,news_c).sendToTarget();
                new Thread(new Runnable() {

                    @Override
                    public void run() {

                        Looper.prepare();
                        handler = new Handler() {
                            @Override
                            public void handleMessage(@NonNull Message msg) {
                                super.handleMessage(msg);
                                //Toast.makeText(SecondActivity.this, msg.obj.toString(), Toast.LENGTH_LONG).show();
                                News n = (News) msg.obj;
                                String news_id=n.getID();
                                NewsAPP mApp = (NewsAPP)getActivity().getApplication();
                                String user_id = mApp.getID();
                                mNewsManager.newsClickEvent(user_id,news_id);
                            }
                        };
                        myLooper = Looper.myLooper();
                        Looper.loop();

                    }
                }).start();


                //跳转页面，并传递参数
                Bundle bundle = new Bundle();
                bundle.putSerializable("news",news_c);
                Intent intent = new Intent(getActivity(), Show_news_Activity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    final Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            MyNewsListAdapter newsAdapter;
            switch (msg.what) {
                case 0:
                    if(myNews.size()!=0){
                        myNews.clear();
                    }
                    myNews = (List<News>)msg.obj;
                    //设置新闻滑动页
                    newsAdapter = new MyNewsListAdapter(getActivity(),R.layout.fragment_news_item,myNews);
                    mLvNews.setAdapter(newsAdapter);
                    break;
                case 1:
                    if(myNews.size()!=0){
                        myNews.clear();
                    }
                    myNews = (List<News>)msg.obj;
                    //设置新闻滑动页
                    newsAdapter = new MyNewsListAdapter(getActivity(),R.layout.fragment_news_item,myNews);
                    mLvNews.setAdapter(newsAdapter);
                    break;
                default:
                    break;
            }
        }

    };


    private void initNews(String myType){
        if(myType.equals("推荐")){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    List<News> mNews = new ArrayList<>();
                    NewsAPP mApp = (NewsAPP) getActivity().getApplication();
                    String id = mApp.getID();
                    mNews = mNewsManager.getRecByUserId(id);
                    Message message = new Message();
                    message.obj =mNews;
                    message.what = 1;
                    mHandler.sendMessage(message);
                }
            }).start();
        }else {
            //从数据库中获取数据
            new Thread(new Runnable() {
                @Override
                public void run() {
                    List<News> mNews = new ArrayList<>();
                    mNews = mNewsManager.getNewsByType(myType);
                    Message message = new Message();
                    message.obj =mNews;
                    message.what = 0;
                    mHandler.sendMessage(message);
                }
            }).start();
        }

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        myLooper.quit();
    }
}
