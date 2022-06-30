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

import java.util.List;

//主页面适配器
public class MyNewsListAdapter extends ArrayAdapter<News>{

    private FragmentActivity myContext;
    private List<News> myNews;
    private int resourceId;

    public MyNewsListAdapter(@NonNull FragmentActivity context, int resource, @NonNull List<News> objects) {
        super(context, resource, objects);
        myContext = context;
        myNews = objects;
        resourceId = resource;
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
            holder.mTvPublish = view.findViewById(R.id.mTvPublish);
            holder.mTvDate = view.findViewById(R.id.mTvDate);
            view.setTag(holder);
        }else{
            view = convertView;
            holder = (ViewHolder)view.getTag();
        }

        holder.mTvTitle.setText(news.getTitle());
        holder.mTvPublish.setText(news.getPublisher());
        holder.mTvDate.setText(news.getDate());

        return view;
    }

    class ViewHolder{
        TextView mTvTitle;
        TextView mTvPublish;
        TextView mTvDate;
    }
}
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.Toast;
//
//import androidx.recyclerview.widget.RecyclerView;
//
//import java.util.List;
//
//public class MyNewsListAdapter extends BaseAdapter {
//
//    private List<News> myNews;
//    private LayoutInflater mInflater;
//    private Callback mCallback;
//
//    public interface Callback{
//        public void click(View v);
//    }
//
//    public MyNewsListAdapter(Context context,List<News> news,Callback callback){
//        myNews = news;
//        mInflater = LayoutInflater.from(context);
//        mCallback = callback;
//    }
//    @Override
//    public int getCount() {
//        return myNews.size();
//    }
//
//    @Override
//    public Object getItem(int position) {
//        return myNews.get(position);
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return position;
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        RecyclerView.ViewHolder vh;
//
//
//        return null;
//    }
//}
