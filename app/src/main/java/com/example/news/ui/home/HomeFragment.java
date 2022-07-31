package com.example.news.ui.home;

import android.Manifest;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
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
import android.os.UserHandle;
import android.os.Vibrator;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextUtils;
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
import com.example.news.NewsAPP;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
        mTvLogo = view.findViewById(R.id.mTvLogo);

        Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(),"AaHouLangXingKai-2.ttf");

        mTvLogo.setTypeface(typeface);

        initTab();
        initTabViewpager();
        initVoice();

        //设置TabLayout和ViewPager联动
        mTlNews.setupWithViewPager(mVpNews,false);

        mIvAdd.setOnClickListener(new View.OnClickListener() {
                @Override
            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), SelectTypeActivity.class);
//                startActivity(intent);
                //getActivity().finish();
//                    getView().announceForAccessibility("可以通过语音输入增加频道，说出“我想要娱乐“，即可添加娱乐频道" +
//                            "一共包含\"财经\",\"彩票\",\"房产\",\"股票\",\"家居\",\"教育\",\"科技\",\"社会\",\"时尚\",\"时政\",\"体育\",\"星座\",\"游戏\",\"娱乐\"" +
//                            "14个频道，按住语音按钮添加我的频道吧");
                    AlertDialog.Builder builder2_1 = new AlertDialog.Builder(getActivity());
                    View v1_1 = LayoutInflater.from(getContext()).inflate(R.layout.edit_talkback, null);
                    Button mBtYes = v1_1.findViewById(R.id.mBtYes);
                    Button mBtNo = v1_1.findViewById(R.id.mBtNo);
                    builder2_1.setTitle("是否在talkback模式下").setView(v1_1);
                    AlertDialog dialog2 = builder2_1.show();

                    mBtYes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog2.dismiss();
                            AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
                            View view3 = View.inflate(getContext(), R.layout.voice_input2, null);
                            TextView mTvVoice = view3.findViewById(R.id.mTvType);
                            mTvVoice.setText("可以通过语音输入增加频道，说出“添加娱乐“，即可添加娱乐频道，" +
                                    "一共包含\"财经\",\"彩票\",\"房产\",\"股票\",\"家居\",\"教育\",\"科技\",\"社会\",\"时尚\",\"时政\",\"体育\",\"星座\",\"游戏\",\"娱乐\"" +
                                    "14个频道，同时也可输入”删除娱乐“，进行删除，按住左下方语音按钮设置我的频道吧");
                            builder1.setView(view3).setPositiveButton("我知道了", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).setTitle("添加频道");
                            builder1.create().show();
                        }
                    });//语音输入借宿

                    mBtNo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog2.dismiss();
                            Intent intent = new Intent(getActivity(), SelectTypeActivity.class);
                            startActivity(intent);
                        }
                    });


            }
        });

        mBtSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AppUsageActivity.class);
                startActivity(intent);
            }
        });

        //搜索按钮
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

                    if(str.contains("添加")){
                        String newType=str.substring(2);
                        Log.e("新的频道",newType);
                        NewsAPP myApp = new NewsAPP();
                        String[] allType = myApp.getNews_type();
                        List<String> list = Arrays.asList(allType);
                        boolean isHave2 = list.contains(newType);
                        boolean isHave = myTypeTab.contains(newType);
                        if(isHave && isHave2){
                            getView().announceForAccessibility(newType+"频道已经存在");
                        }else if(!isHave2){
                            getView().announceForAccessibility(newType+"频道不存在" +
                                    "请从\"财经\",\"彩票\",\"房产\",\"股票\",\"家居\",\"教育\",\"科技\",\"社会\",\"时尚\",\"时政\",\"体育\",\"星座\",\"游戏\",\"娱乐\"" +
                                    "中选择");
                        }else {
                            myTypeManager.insertType(newType);
                            myTypeTab.add(newType);
                            mTlNews.removeAllTabs();
                            initTab();
                            initTabViewpager();
                            mVpNews.getAdapter().notifyDataSetChanged();
                            getView().announceForAccessibility(newType+"频道添加成功");
                        }

                    }else if(str.contains("删除")){
                        String oldType=str.substring(2);
                        Log.e("旧的频道",oldType);
                        NewsAPP myApp = new NewsAPP();
                        String[] allType = myApp.getNews_type();
                        boolean isHave = myTypeTab.contains(oldType);
                        if(!isHave){
                            getView().announceForAccessibility(oldType+"频道不存在");
                        }else {
                            myTypeManager.deleteMyType(oldType);
                            myTypeTab.remove(oldType);
                            mTlNews.removeAllTabs();
                            initTab();
                            mVpNews.getAdapter().notifyDataSetChanged();
                            getView().announceForAccessibility(oldType+"频道已删除");
                        }

                    }
                    else{
                        mEtSearch.setText(str);
                        //Toast.makeText(getContext(), "语音输入已完成可以按搜索键开始查询了",Toast.LENGTH_SHORT).show();
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        View view2 = View.inflate(getContext(), R.layout.voice_input, null);
                        TextView mTvVoice = view2.findViewById(R.id.mTvVoice);
                        if(str.equals("")){
                            mTvVoice.setText("空");
                        }else {
                            mTvVoice.setText(str);
                        }
                        builder.setView(view2).setPositiveButton("开始查询", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
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
                        }).setNegativeButton("取消查询", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //不做处理
                            }
                        }).setTitle("语音输入结果为");

                        builder.create().show();
                    }


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
        mFbRadioStart.setOnLongClickListener(new startRecordListener());
        mFbRadioStart.setOnClickListener(new stopRecordListener());

    }

    //长按录音，松开后自动执行短按操作
    class startRecordListener implements View.OnLongClickListener {
        @Override
        public boolean onLongClick(View v) {
            isLongClick = 1;
            Vibrator vib = (Vibrator)getActivity().getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
            vib.vibrate(20);//只震动一秒，一次
            myVoice.startRecord(myVoice.getFileName());
            return false; //KeyPoint：setOnLongClickListener中return的值决定是否在长按后再加一个短按动作，true为不加短按,false为加入短按
        }
    }
    //短按停止录音，直接点击短按无效
    class stopRecordListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if(isLongClick==1){
                myVoice.stopRecord();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            getView().announceForAccessibility("正在分析语音");
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
            isLongClick=0;

        }
    }

//    public static boolean getTalkBackState(Context context) {
//        String TalkBackpackageName = "com.google.android.marvin.talkback";
//        String TalkBackServiceName = "com.google.android.marvin.talkback.TalkBackService";
//        final boolean accessibilityEnabled = Settings.Secure.getInt(context.getContentResolver(),
//                Settings.Secure.ACCESSIBILITY_ENABLED, 0) == 1;
//        Set<ComponentName> enabledServices = getEnabledServicesFromSettings(context, UserHandle.getUserId());
//        boolean talkBackState = accessibilityEnabled && enabledServices.contains(new ComponentName(TalkBackpackageName, TalkBackServiceName));
//        return talkBackState;
//    }
//
//    public static Set<ComponentName> getEnabledServicesFromSettings(Context context, int userId) {
//        final String enabledServicesSetting = Settings.Secure.getStringForUser(
//                context.getContentResolver(), Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES,
//                userId);
//        if (enabledServicesSetting == null) {
//            return Collections.emptySet();
//        }
//
//        final Set<ComponentName> enabledServices = new HashSet<>();
//        final TextUtils.SimpleStringSplitter colonSplitter = new TextUtils.SimpleStringSplitter(':');
//        colonSplitter.setString(enabledServicesSetting);
//
//        while (colonSplitter.hasNext()) {
//            final String componentNameString = colonSplitter.next();
//            final ComponentName enabledService = ComponentName.unflattenFromString(
//                    componentNameString);
//            if (enabledService != null) {
//                enabledServices.add(enabledService);
//            }
//        }
//        return enabledServices;
//    }



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

    //fragment重新刷新的方法
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