package com.example.news.ui.user;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.AbstractWindowedCursor;
import android.database.Cursor;
import android.database.CursorWindow;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import android.os.Message;

import com.example.news.NewsAPP;
import com.example.news.R;
import com.example.news.UserData;
import com.example.news.UserDataManager;

public class UserFragment extends Fragment {

    public static final int MODE_PRIVATE = 0;

    private UserViewModel notificationsViewModel;
    private UserViewModel mViewModel;
    private String[] set = new String[]{"APP设置","使用指南","联系我们","问题与建议"};

    private String[] mine = new String[]{"个人资料编辑"};


    private TextView mTvSet;
    private TextView mTvUse;
    private TextView mTvAnswer;
    private TextView mTvSuggest;
    private TextView mTvName; //姓名
    private UserDataManager mUserDataManager;
    private androidx.constraintlayout.widget.ConstraintLayout mTvUser;
    private LinearLayout mLlComment;
    private LinearLayout mLlCollect;
    private LinearLayout mLlHistory;
    private LinearLayout mLlMessage;


    public static UserFragment newInstance() {
        return new UserFragment();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                new ViewModelProvider(this).get(UserViewModel.class);
        View root = inflater.inflate(R.layout.fragment_user, container, false);

        notificationsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

                //textView.setText(s);
            }
        });
        return root;

    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (mUserDataManager == null) {
            mUserDataManager = new UserDataManager(getActivity());
            //mUserDataManager.openDataBase();                              //建立本地数据库
        }

        initView();
        initData();
        setListeners();
    }



    private void initView(){
        mTvUser = getActivity().findViewById(R.id.mTvUser);
        mTvName = getActivity().findViewById(R.id.mTvName);
        mTvSuggest = getActivity().findViewById(R.id.mTvSuggest);
        mTvAnswer = getActivity().findViewById(R.id.mTvAnswer);
        mTvSet = getActivity().findViewById(R.id.mTvSet);
        mTvUse = getActivity().findViewById(R.id.mTvUse);
        mLlCollect = getActivity().findViewById(R.id.mLlCollect);
        mLlComment = getActivity().findViewById(R.id.mLlComment);
        mLlHistory = getActivity().findViewById(R.id.mLlHistory);
        mLlMessage = getActivity().findViewById(R.id.mLlMessage);
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    private void initData(){
        getDataFromSpf();
    }

        final Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    UserData user = (UserData) msg.obj;
                    mTvName.setText(user.getName());
                    break;
                default:
                    break;
            }
        }

    };

    @RequiresApi(api = Build.VERSION_CODES.P)
    private void getDataFromSpf() {
        //final UserApp app =(UserApp)getActivity().getApplication();
        new Thread(new Runnable() {
            @Override
            public void run() {
                NewsAPP app = (NewsAPP) getActivity().getApplication();
                String id = app.getID();
                UserData mUser = mUserDataManager.fetchUserData(id);
                Message message = new Message();
                message.obj =mUser;
                message.what = 0;
                mHandler.sendMessage(message);
            }
        }).start();
//        NewsAPP app = (NewsAPP) getActivity().getApplication();
//        UserData mUser = app.getMyUser();
//        mTvName.setText(mUser.getName());
    }

    private void setListeners() {
        OnClick onClick = new OnClick();
        mTvUser.setOnClickListener(onClick);
        mTvUse.setOnClickListener(onClick);
        mTvAnswer.setOnClickListener(onClick);
        mTvSuggest.setOnClickListener(onClick);
        mTvSet.setOnClickListener(onClick);

        mLlComment.setOnClickListener(onClick);
        mLlCollect.setOnClickListener(onClick);
        mLlHistory.setOnClickListener(onClick);
        mLlMessage.setOnClickListener(onClick);
    }

    //重写一个类
    class OnClick implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            Intent intent = null;
            switch (view.getId()) {
                case R.id.mTvUser:
                    intent = new Intent(getActivity(), PeopleInfoActivity.class);
                    startActivity(intent);
                    break;
                case R.id.mTvSet:
                    intent = new Intent(getActivity(), AppSettingActivity.class);
                    startActivity(intent);
                    break;

                case R.id.mTvUse:
                    intent = new Intent(getActivity(), AppUsageActivity.class);
                    startActivity(intent);
                    break;

                case R.id.mTvAnswer:
                    intent = new Intent(getActivity(), ContactUsActivity.class);
                    startActivity(intent);
                    break;

                case R.id.mTvSuggest:
                    intent = new Intent(getActivity(), QAAActivity.class);
                    startActivity(intent);
                    break;

            }
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public void onResume() { //对数据的重新加载,进行更新
        super.onResume();
        if (mUserDataManager == null) {
            mUserDataManager = new UserDataManager(getActivity());
            //mUserDataManager.openDataBase();                              //建立本地数据库
        }
        initData();
    }

    class MyBaseAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return set.length;
        }

        @Override
        public Object getItem(int position) {
            return set[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view=LayoutInflater.from(getActivity()).inflate(R.layout.activity_set_item,parent,false);
            TextView textView = (TextView)view.findViewById(R.id.mTvPeople);
            ImageView imageView = (ImageView)view.findViewById(R.id.people_image);
            //ImageView mIvArrow=(ImageView)view.findViewById(R.id.mIvArrow);
            textView.setText(set[position]);
            //imageView.setBackgroundResource(icons[position]);
            // imageView.setBackgroundResource(icons_2[position]);
            return view;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mUserDataManager != null) {
            //mUserDataManager.closeDataBase();
            mUserDataManager = null;
        }
    }

    class MyMineAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return mine.length;
        }

        @Override
        public Object getItem(int position) {
            return mine[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view=LayoutInflater.from(getActivity()).inflate(R.layout.activity_mine_item,parent,false);
            TextView textView = (TextView)view.findViewById(R.id.mTvPeople);
            ImageView imageView = (ImageView)view.findViewById(R.id.people_image);

            textView.setText(mine[position]);
            //imageView.setBackgroundResource(icons_2[position]);

            return view;
        }
    }
}