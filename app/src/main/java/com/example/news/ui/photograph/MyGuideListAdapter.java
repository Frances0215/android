package com.example.news.ui.photograph;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.example.news.R;
import com.example.news.ui.home.MyNewsListAdapter;
import com.example.news.ui.home.News;

import java.util.Collections;
import java.util.List;

public class MyGuideListAdapter extends ArrayAdapter<AddressBean> {

    private Activity myContext;
    private List<AddressBean> myGuide;
    private int resourceId;

    public MyGuideListAdapter(@NonNull Activity context, int resource, @NonNull List<AddressBean> objects) {
        super(context, resource, objects);
        myContext = context;
        Collections.reverse(objects);
        myGuide = objects;
        resourceId = resource;
    }

    public void setMyAddressBean(List<AddressBean> guide){
        Collections.reverse(guide);
        this.myGuide = guide;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final AddressBean guide = getItem(position);
        View view;
        MyGuideListAdapter.ViewHolder holder = new MyGuideListAdapter.ViewHolder();
        if(convertView == null){
            view= LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
            holder.mTvGuide = view.findViewById(R.id.mTvGuide);
            //holder.mTvDate = view.findViewById(R.id.mTvDate);
            view.setTag(holder);
        }else{
            view = convertView;
            holder = (MyGuideListAdapter.ViewHolder)view.getTag();
        }

        holder.mTvGuide.setText(guide.getText());


        return view;
    }

    class ViewHolder{
        TextView mTvGuide;
    }
}


