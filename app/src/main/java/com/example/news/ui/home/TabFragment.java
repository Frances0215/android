package com.example.news.ui.home;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.news.DBUtils;
import com.example.news.LoginActivity;
import com.example.news.NewsAPP;
import com.example.news.NewsManager;
import com.example.news.R;
import com.example.news.UserData;
import com.example.news.UserDataManager;
import com.example.news.ui.MyListView;

import java.util.ArrayList;
import java.util.List;

//滑动页fragment
public class TabFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    private ListView mLvNews;
    private List<News> myNews = new ArrayList<>();
    private NewsManager mNewsManager;
    private MyNewsListAdapter newsAdapter;
    private SwipeRefreshLayout swipeLayout;
    private boolean isRefresh = false;
    private String lastNewsID = "";
    private String user_id;
    private String label;

    private Looper myLooper;
    private NewsAPP mApp ;

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

        mApp= (NewsAPP) getActivity().getApplication();
        user_id = mApp.getID();
        mLvNews = getView().findViewById(R.id.mLvNews);
        swipeLayout = getView().findViewById(R.id.swipeRefreshLayout);

        swipeLayout.setOnRefreshListener(this);
        //新闻类别
        label = getArguments().getString("label");
        if(mApp.getMap().get(label)==null && label.equals("推荐")){
            int startNum = (int) (Math.random() * (50));
            mApp.getMap().put(label,startNum);
        }else if(mApp.getMap().get(label)==null){
            int startNum = (int) (Math.random() * (10));
            mApp.getMap().put(label,startNum);
        }
        initNews(label,0);


        ClickThread myThread = new ClickThread();
        myThread.start();

        mLvNews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                News news_c = (News) parent.getItemAtPosition(position);

                Message msg = Message.obtain();
                msg.obj = news_c;
                myThread.handler.sendMessage(msg);

                //跳转页面，并传递参数
                Bundle bundle = new Bundle();
                bundle.putSerializable("news",news_c);
                Intent intent = new Intent(getActivity(), Show_news_Activity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });


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
                    Log.i("子线程",n.getID());
                    String news_id=n.getID();
                    NewsAPP mApp = (NewsAPP)getActivity().getApplication();
                    String user_id = mApp.getID();
                    mNewsManager.newsClickEvent(user_id,news_id);
                }
            };
            myLooper = Looper.myLooper();
            Looper.loop();

        }
    }


    @Override
    public void onStart() {
        super.onStart();

    }

    final Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case 0:
                    List<News> news_temp;
                    news_temp=(List<News>)msg.obj;
                    myNews.addAll(news_temp);
                    //设置新闻滑动页
                    newsAdapter = new MyNewsListAdapter(getActivity(),R.layout.fragment_news_item,myNews);
                    mLvNews.setAdapter(newsAdapter);

                    break;
                case 1:
                    List<News> news_temp2;
                    news_temp2=(List<News>)msg.obj;
                    myNews.addAll(news_temp2);
                    if(news_temp2.size()==0)
                        Toast.makeText(getActivity(), "已经是最新数据啦",Toast.LENGTH_SHORT).show();
                    //设置新闻滑动页
//                    newsAdapter = new MyNewsListAdapter(getActivity(),R.layout.fragment_news_item,myNews);
//                    mLvNews.setAdapter(newsAdapter);
                    break;
                default:
                    break;
            }
        }

    };


    private void initNews(String myType,int flag){
        if(myType.equals("推荐")){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    List<News> mNews = new ArrayList<>();
                    SharedPreferences login_sp;
                    login_sp = getActivity().getSharedPreferences("userInfo", 0);
                    String user_id2=login_sp.getString("USER_ID", "");

                    mNews = mNewsManager.getRecByUserId(user_id2,mApp.getMap().get(label));
                    Message message = new Message();
                    message.obj =mNews;
                    message.what = flag;
                    mHandler.sendMessage(message);
                }
            }).start();
        }else {
            //从数据库中获取数据
            new Thread(new Runnable() {
                @Override
                public void run() {
                    List<News> mNews = new ArrayList<>();
                    mNews = mNewsManager.getNewsByType(myType,mApp.getMap().get(label));
                    Message message = new Message();
                    message.obj =mNews;
                    message.what = 0;
                    mHandler.sendMessage(message);
                }
            }).start();
        }

    }

    public void onRefresh() {
        if(!isRefresh){
            isRefresh = true;
            getView().announceForAccessibility("正在刷新");
        new Handler().postDelayed(new Runnable() {
            public void run() {
                int a = mApp.getMap().remove(label);
                a++;
                mApp.getMap().put(label,a);
                initNews(label,1);
                swipeLayout.setRefreshing(false);
                newsAdapter.setMyNews(myNews);
                //list.add(new SoftwareClassificationInfo(2, "ass"));
                newsAdapter.notifyDataSetChanged();
                isRefresh= false;
            }
        }, 1000); }
        getView().announceForAccessibility("刷新完毕");
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


