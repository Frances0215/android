package com.example.news.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.news.R;

import java.util.Collections;
import java.util.List;

//主页面适配器
public class MyNewsListAdapter extends ArrayAdapter<News>{

    private FragmentActivity myContext;
    private List<News> myNews;
    private int resourceId;

    public MyNewsListAdapter(@NonNull FragmentActivity context, int resource, @NonNull List<News> objects) {
        super(context, resource, objects);
        myContext = context;
        Collections.reverse(objects);
        myNews = objects;
        resourceId = resource;
    }

    public void setMyNews(List<News> news){
        Collections.reverse(news);
        this.myNews = news;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final News news = getItem(position);
        View view;
        ViewHolder holder = new ViewHolder();
        if(convertView == null){
            view= LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
            holder.mTvTitle = view.findViewById(R.id.mTvTitle);
            holder.mTvDate = view.findViewById(R.id.mTvDate);
            view.setTag(holder);
        }else{
            view = convertView;
            holder = (ViewHolder)view.getTag();
        }

        holder.mTvTitle.setText(news.getTitle());
        holder.mTvDate.setText(news.getPublishTime());

        return view;
    }

    class ViewHolder{
        TextView mTvTitle;
        TextView mTvDate;
    }
}

