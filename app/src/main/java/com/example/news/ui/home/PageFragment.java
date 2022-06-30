package com.example.news.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.news.R;

public class PageFragment extends Fragment {

    private TextView mTvContents;
    private News myNews;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_page, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTvContents=(TextView) view.findViewById(R.id.mTvContents);
        int label = getArguments().getInt("label");
        myNews = (News) getArguments().getSerializable("news");
        String[] content = myNews.getContent_f();
        if(label==0){
            mTvContents.setText(myNews.getTitle()+"\n"+myNews.getPublisher()+myNews.getDate());
        }else
            mTvContents.setText(content[label-1]);

    }

    public static PageFragment newInstance(int label,News myNews) {
        Bundle args = new Bundle();
        args.putInt("label", label);
        args.putSerializable("news",myNews);
        PageFragment fragment = new PageFragment();
        fragment.setArguments(args);
        return fragment;
    }

}
