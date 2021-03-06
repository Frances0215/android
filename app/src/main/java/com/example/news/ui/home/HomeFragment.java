package com.example.news.ui.home;

import android.Manifest;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
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
import com.example.news.DropDownEditText;
import com.example.news.FontIconView;
import com.example.news.NewsManager;
import com.example.news.R;
import com.example.news.TypeManager;
import com.example.news.VoiceRecord;
import com.example.news.VoiceTrans;
import com.example.news.ui.user.AppUsageActivity;
import com.google.android.material.tabs.TabLayout;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    private DropDownEditText mEtSearch;
    private TabLayout mTlNews;
    private ViewPager mVpNews;
    private DBUtils myDb ;
    private FontIconView mIvAdd;
    private Button mFbRadioStart;
    private Button mBtSearch;
    private TextView mTvLogo;
    //private FloatingActionButton mFbRadioStop;
    //tab?????????????????????????????????type???????????????????????????
    private ArrayList<String> myTypeTab = new ArrayList<>();
    private List<TabFragment> tabFragmentList = new ArrayList<>();
    private List<News> myNews;
//    private final String FILE_NAME = getContext().getFilesDir().getPath().toString()+"/test.pcm";
    private TypeManager myTypeManager;

    private VoiceTrans myTrans = new VoiceTrans();
    private VoiceRecord myVoice = new VoiceRecord();
    //private Thread recordingThread;
    private NewsManager mNewsManager;

    private int isLongClick=0;


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

        //??????????????????
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
        mTvLogo = view.findViewById(R.id.mTvLogo);

        Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(),"AaHouLangXingKai-2.ttf");

        mTvLogo.setTypeface(typeface);

        initTab();
        initTabViewpager();
        initVoice();

        //??????TabLayout???ViewPager??????
        mTlNews.setupWithViewPager(mVpNews,false);

        mIvAdd.setOnClickListener(new View.OnClickListener() {
                @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SelectTypeActivity.class);
                startActivity(intent);
                //getActivity().finish();
            }
        });

        mBtSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AppUsageActivity.class);
                startActivity(intent);
            }
        });

        //????????????
        mEtSearch.setOnDropArrowClickListener(new DropDownEditText.OnDropArrowClickListener() {
            @Override
            public void onDropArrowClick() {
                mEtSearch.setFocusableInTouchMode(false);
                mEtSearch.setFocusable(false);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String keyWord = String.valueOf(mEtSearch.getText());
                        //mNewsManager.searchByWord(keyWord);
                        //??????????????????????????????
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

    //??????????????????????????????????????????
    final Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    String searchFor = (String) msg.obj;
                    String str=searchFor.replaceAll("[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~???@#???%??????& amp;*????????????+|{}????????????????????????????????????|-]", "");

                    if(str.contains("?????????")){
                        String newType=str.substring(3);
                        Log.e("????????????",newType);
                        boolean isHave = myTypeTab.contains(newType);
                        if(isHave){
                            getView().announceForAccessibility(newType+"??????????????????");
                        }else {
                            myTypeManager.insertType(newType);
                            myTypeTab.add(newType);
                            mTlNews.removeAllTabs();
                            initTab();
                            mVpNews.getAdapter().notifyDataSetChanged();
                            getView().announceForAccessibility(newType+"??????????????????");
                        }

                    }else{
                        mEtSearch.setText(str);
                        //Toast.makeText(getContext(), "??????????????????????????????????????????????????????",Toast.LENGTH_SHORT).show();
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        View view2 = View.inflate(getContext(), R.layout.voice_input, null);
                        TextView mTvVoice = view2.findViewById(R.id.mTvVoice);
                        if(str.equals("")){
                            mTvVoice.setText("???");
                        }else {
                            mTvVoice.setText(str);
                        }
                        builder.setView(view2).setPositiveButton("????????????", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        String keyWord = String.valueOf(mEtSearch.getText());
                                        //mNewsManager.searchByWord(keyWord);
                                        //??????????????????????????????
                                        Bundle bundle = new Bundle();
                                        bundle.putSerializable("keyWord", keyWord);
                                        Intent intent = new Intent(getActivity(), SearchResultActivity.class);
                                        intent.putExtras(bundle);
                                        startActivity(intent);
                                    }
                                }).start();
                            }
                        }).setNegativeButton("????????????", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //????????????
                            }
                        }).setTitle("?????????????????????");

                        builder.create().show();
                    }


                    break;
                default:
                    break;
            }
        }

    };


    private void initTabViewpager(){
        //???????????????
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
                    return "??????";
                else
                    return myTypeTab.get(position-1);
            }
        });

        //???????????????
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
        //??????tab
        //??????????????????????????????
        TabLayout.Tab tab = mTlNews.newTab();
        mTlNews.addTab(tab.setText("??????"));
        tabFragmentList.add(TabFragment.newInstance("??????"));
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

        //??????????????????
        mFbRadioStart.setOnLongClickListener(new startRecordListener());
        mFbRadioStart.setOnClickListener(new stopRecordListener());

    }

    //????????????????????????????????????????????????
    class startRecordListener implements View.OnLongClickListener {
        @Override
        public boolean onLongClick(View v) {
            isLongClick = 1;
            Vibrator vib = (Vibrator)getActivity().getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
            vib.vibrate(20);//????????????????????????
            myVoice.startRecord(myVoice.getFileName());
            return false; //KeyPoint???setOnLongClickListener???return?????????????????????????????????????????????????????????true???????????????,false???????????????
        }
    }
    //?????????????????????????????????????????????
    class stopRecordListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if(isLongClick==1){
                myVoice.stopRecord();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String result = myTrans.voiceTrans(myVoice.getFileName());
                            //String result = "??????";
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
            isLongClick=0;

        }
    }


    @Override
    public void onResume() {
        if(myTypeManager == null){
            myTypeManager = new TypeManager(getActivity());
            myTypeManager.openDataBase();
        }

        super.onResume();
//        myTypeTab = myTypeManager.getAllMyType();
//        //mVpNews.getAdapter().notifyDataSetChanged();
//        initTab();
//        initTabViewpager();

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

    //fragment?????????????????????
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(getActivity());
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.CART_BROADCAST");
        BroadcastReceiver mItemViewListClickReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent){
                String msg = intent.getStringExtra("data");
                if("refresh".equals(msg)){
                    refresh();
                }
            }
        };
        broadcastManager.registerReceiver(mItemViewListClickReceiver, intentFilter);
    }

    private void refresh() {
        myTypeTab = myTypeManager.getAllMyType();
        initTab();
        //initTabViewpager();
        mVpNews.getAdapter().notifyDataSetChanged();
    }
}