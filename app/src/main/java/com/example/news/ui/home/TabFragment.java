package com.example.news.ui.home;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
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
import com.example.news.R;

import java.util.ArrayList;
import java.util.List;

//滑动页fragment
public class TabFragment extends Fragment {

    private ListView mLvNews;
    private List<News> myNews = new ArrayList<>();
    private DBUtils myDbUntil;

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

        mLvNews = getView().findViewById(R.id.mLvNews);
        String label = getArguments().getString("label");
        initNews(label);
        //设置新闻滑动页
        MyNewsListAdapter newsAdapter = new MyNewsListAdapter(getActivity(),R.layout.fragment_news_item,myNews);
        mLvNews.setAdapter(newsAdapter);
        mLvNews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                News news_c = (News) parent.getItemAtPosition(position);

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

    private void initNews(String myType){
        //从数据库中获取数据
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                myNews = myDbUntil.getNewsByType(myType);
//            }
//        }).start();
        //News news_temp = new News(null,null,null,null,null,null);
        String ID="";
        switch (myType){
            case "推荐":
                if(myNews.size()!=0)
                    myNews.clear();
                for(int i=0;i<20;i++){
                    //ID="T"+i;
                    String content = "苹果  梨子  香蕉";
                    News news_temp = new News("T"+i,"推荐新闻","新华社","2022-05-24",content,"推荐");
//                    news_temp.setID("T");
//                    news_temp.setTitle("推荐新闻");
//                    news_temp.setContents(content);
//                    news_temp.setDate("2022-05-24");
//                    news_temp.setPublisher("新华社");
//                    news_temp.setType("推荐");
                    myNews.add(news_temp);
                }
                break;
            case "实时":
                if(myNews != null)
                    myNews.clear();
                for(int i=0;i<20;i++){
                    String content = "苹果  梨子  香蕉";
                    News news_temp = new News("T","实时新闻","人民日报社","2022-05-24",content,"实时");
                    myNews.add(news_temp);
                }
                break;
            case "政治":
                if(myNews != null)
                    myNews.clear();
                for(int i=0;i<20;i++){
                    String content = "苹果  梨子  香蕉";
                    News news_temp = new News("T","政治新闻","今日头条","2022-05-24",content,"政治");
                    myNews.add(news_temp);
                }
                break;
            case "军事":
                if(myNews != null)
                    myNews.clear();
                for(int i=0;i<20;i++){
                    String content = "苹果  梨子  香蕉";
                    News news_temp = new News("T","军事新闻","封面新闻","2022-05-24",content,"军事");
                    myNews.add(news_temp);
                }
                break;
            case "娱乐":
                if(myNews != null)
                    myNews.clear();
                for(int i=0;i<20;i++){
                    String content = "苹果  梨子  香蕉";
                    News news_temp = new News("T","娱乐新闻","青年日报","2022-05-24",content,"娱乐");
                    myNews.add(news_temp);
                }
                break;
            case "法律":
                if(myNews != null)
                    myNews.clear();
                for(int i=0;i<20;i++){
                    String content = "苹果  梨子  香蕉";
                    News news_temp = new News("T","法律新闻","人民日报社","2022-05-24",content,"法律");
                    myNews.add(news_temp);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }
}
