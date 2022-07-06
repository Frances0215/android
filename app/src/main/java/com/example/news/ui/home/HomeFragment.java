package com.example.news.ui.home;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.example.news.DBUtils;
import com.example.news.FontIconView;
import com.example.news.NewsManager;
import com.example.news.R;
import com.example.news.TypeManager;
import com.example.news.VoiceRecord;
import com.example.news.VoiceTrans;
import com.google.android.material.tabs.TabLayout;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    private EditText mEtSearch;
    private TabLayout mTlNews;
    private ViewPager mVpNews;
    private DBUtils myDb ;
    private FontIconView mIvAdd;
    private Button mFbRadioStart;
    private Button mBtSearch;

    //private FloatingActionButton mFbRadioStop;
    //tab内容，这只是数据库中的type类型不包括推荐类型
    private ArrayList<String> myTypeTab = new ArrayList<>();
    private List<TabFragment> tabFragmentList = new ArrayList<>();
    private List<News> myNews;
//    private final String FILE_NAME = getContext().getFilesDir().getPath().toString()+"/test.pcm";
    private TypeManager myTypeManager;

    private VoiceTrans myTrans = new VoiceTrans();
    private VoiceRecord myVoice = new VoiceRecord();
    //private Thread recordingThread;
    private NewsManager mNewsManager;


    public HomeFragment() {
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

            }
        });
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //初始化数据库
        if(myTypeManager == null){
            myTypeManager = new TypeManager(getActivity());
            myTypeManager.openDataBase();
        }

        if(mNewsManager == null){
            mNewsManager = new NewsManager(getActivity());
        }

        myTypeTab = myTypeManager.getAllMyType();

        mTlNews = view.findViewById(R.id.tab_layout);
        mVpNews = view.findViewById(R.id.view_pager);
        mIvAdd = view.findViewById(R.id.mIvAdd);
        mFbRadioStart = view.findViewById(R.id.mFbRadioStart);
        mEtSearch = view.findViewById(R.id.mEtSearch);
        mBtSearch = view.findViewById(R.id.mBtRefresh);

        initTab();
        initTabViewpager();
        initVoice();

        //设置TabLayout和ViewPager联动
        mTlNews.setupWithViewPager(mVpNews,false);

        mIvAdd.setOnClickListener(new View.OnClickListener() {
                @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SelectTypeActivity.class);
                startActivity(intent);
            }
        });

        mBtSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String keyWord = String.valueOf(mEtSearch.getText());
                        //mNewsManager.searchByWord(keyWord);
                        //跳转页面，并传递参数
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("keyWord", keyWord);
                        Intent intent = new Intent(getActivity(), SearchResultActivity.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                }).start();
            }
        });


    }

    //用于将语音输入，写入到搜索框
    final Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    String searchFor = (String) msg.obj;
                    String str=searchFor.replaceAll("[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……& amp;*（）——+|{}【】‘；：”“’。，、？|-]", "");
                    mEtSearch.setText(str);
                    Toast.makeText(getContext(), "语音输入已完成可以按搜索键开始查询了",Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }

    };


    private void initTabViewpager(){
        //添加适配器
        mVpNews.setAdapter(new FragmentPagerAdapter(getChildFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                return tabFragmentList.get(position);
            }

            @Override
            public int getCount() {
                return tabFragmentList.size();
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                if(position == 0)
                    return "推荐";
                else
                    return myTypeTab.get(position-1);
            }
        });

        //添加监听器
        mVpNews.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //initNews(tabs[position]);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initTab(){
        //添加tab
        //推荐类型固定，不更改
        TabLayout.Tab tab = mTlNews.newTab();
        mTlNews.addTab(tab.setText("推荐"));
        tabFragmentList.add(TabFragment.newInstance("推荐"));
        for (int i = 0; i < myTypeTab.size(); i++) {
            TabLayout.Tab tab2 = mTlNews.newTab();
            mTlNews.addTab(tab2.setText(myTypeTab.get(i)));
            tabFragmentList.add(TabFragment.newInstance(myTypeTab.get(i)));
        }

    }

    private void initVoice(){
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1000);
        }
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.RECORD_AUDIO}, 2000);
        }

        //语音输入开始
        mFbRadioStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myVoice.startRecord(myVoice.getFileName());
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                View view2 = View.inflate(getContext(), R.layout.voice_input, null);
                final LinearLayout mLlVoice = (LinearLayout) view2.findViewById(R.id.mLlVoice);
                builder.setView(view2).setPositiveButton("结束语音输入", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        myVoice.stopRecord();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    String result = myTrans.voiceTrans(myVoice.getFileName());
                                    //String result = "哈哈";
                                    Message message = new Message();
                                    message.obj =result;
                                    message.what = 0;
                                    mHandler.sendMessage(message);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                    }
                });

                builder.create();
                AlertDialog dialog = builder.show();
                dialog.getWindow().setLayout(1000,800);

                //设置确定按钮的位置大小
                Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positiveButton.setTextSize(35);
                positiveButton.setTextColor(Color.BLACK);
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) positiveButton.getLayoutParams();
                layoutParams.weight = 10;
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setLayoutParams(layoutParams);
                //设置使点击空白处不能关闭
                dialog.setCanceledOnTouchOutside(false);
                mLlVoice.setOnClickListener(new View.OnClickListener() {

                    @Override

                    public void onClick(View v) {
                        myVoice.stopRecord();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    String result = myTrans.voiceTrans(myVoice.getFileName());
                                    //String result = myTrans.voiceTrans(FILE_NAME);
                                    //String result="哈哈";
                                    Message message = new Message();
                                    message.obj =result;
                                    message.what = 0;
                                    mHandler.sendMessage(message);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                        dialog.dismiss();
                    }
                });

            }
        });

    }

//    private void initRefresh(){
//        mBtRefresh.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
//    }

    @Override
    public void onResume() {
        if(myTypeManager == null){
            myTypeManager = new TypeManager(getActivity());
            myTypeManager.openDataBase();
        }

        super.onResume();
        mVpNews.getAdapter().notifyDataSetChanged();

    }

    @Override
    public void onPause() {
        if(myTypeManager != null){
            myTypeManager.closeDataBase();
            myTypeManager=null;
        }
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if(!hidden){

        }
        super.onHiddenChanged(hidden);
    }
}