package com.example.news.ui.user;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.news.R;

import java.util.List;

class ChatAdapter extends BaseAdapter
{

    private Context mContext;
    private List<Message> mData;

    public ChatAdapter(Context context,List<Message> data)
    {
        this.mContext=context;
        this.mData=data;
    }

    public void Refresh()
    {
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount()
    {
        return mData.size();
    }

    @Override
    public Object getItem(int Index)
    {
        return mData.get(Index);
    }

    @Override
    public long getItemId(int Index)
    {
        return Index;
    }

    @Override
    public View getView(int Index, View mView, ViewGroup mParent)
    {
        TextView Content;
        switch(mData.get(Index).getType())
        {
            case Message.MessageType_Time:
                mView=LayoutInflater.from(mContext).inflate(R.layout.message_time_layout, null);
                Content=(TextView)mView.findViewById(R.id.Time);
                Content.setText(mData.get(Index).getContent());
                break;
            case Message.MessageType_From:
                mView = LayoutInflater.from(mContext).inflate(R.layout.from_layout, null);
                Content = mView.findViewById(R.id.From_Content);
                Content.setText(mData.get(Index).getContent());
                break;
            case Message.MessageType_To:
                mView=LayoutInflater.from(mContext).inflate(R.layout.to_layout, null);
                Content=(TextView)mView.findViewById(R.id.To_Content);
                Content.setText(mData.get(Index).getContent());
                break;
        }
        return mView;
    }

}