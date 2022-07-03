package com.example.news.ui.home;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.example.news.DBUtils;
import com.example.news.FontIconView;
import com.example.news.R;
import com.example.news.TypeManager;
import com.example.news.VoiceTrans;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    private EditText mEtSearch;
    private TabLayout mTlNews;
    private ViewPager mVpNews;
    private DBUtils myDb ;
    private FontIconView mIvAdd;
    private FloatingActionButton mFbRadioStart;
    private FloatingActionButton mFbRadioStop;
    //tab内容，这只是数据库中的type类型不包括推荐类型
    private ArrayList<String> myTypeTab = new ArrayList<>();
    private List<TabFragment> tabFragmentList = new ArrayList<>();
    private List<News> myNews;

    private TypeManager myTypeManager;


    private static final String FILE_NAME = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC) + File.separator + "test.pcm";

    private AudioRecord audioRecord;
    private int recordBufsize = 0;
    private boolean isRecording = false;

    private VoiceTrans myTrans = new VoiceTrans();

    private Thread recordingThread;


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

        myTypeTab = myTypeManager.getAllMyType();

        mTlNews = view.findViewById(R.id.tab_layout);
        mVpNews = view.findViewById(R.id.view_pager);
        mIvAdd = view.findViewById(R.id.mIvAdd);
        mFbRadioStart = view.findViewById(R.id.mFbRadioStart);
        mFbRadioStop = view.findViewById(R.id.mFbRadioStop);

        initTab();
        initTabViewpager();

        //设置TabLayout和ViewPager联动
        mTlNews.setupWithViewPager(mVpNews,false);

        mIvAdd.setOnClickListener(new View.OnClickListener() {
                @Override
            public void onClick(View v) {
//                    getActivity().getSupportFragmentManager()
//                            .beginTransaction()
//                            .replace(R.id.fragment_home, new SelectTypeFragment(), null)
//                            .addToBackStack(null)
//                            .commit();
                Intent intent = new Intent(getActivity(), SelectTypeActivity.class);
                startActivity(intent);
            }
        });

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1000);
        }
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.RECORD_AUDIO}, 2000);
        }

        createAudioRecord();

        //语音输入
        mFbRadioStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRecord();
            }
        });

        mFbRadioStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopRecord();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                myTrans.voiceTrans(FILE_NAME);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();


            }
        });


    }

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

    private void createAudioRecord() {
        recordBufsize = AudioRecord
                .getMinBufferSize(44100,
                        AudioFormat.CHANNEL_IN_MONO,
                        AudioFormat.ENCODING_PCM_16BIT);
        Log.i("audioRecordTest", "size->" + recordBufsize);
        audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC,
                44100,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT,
                recordBufsize);
    }

    private void startRecord() {
        if (isRecording) {
            return;
        }
        isRecording = true;
        audioRecord.startRecording();
        Log.i("audioRecordTest", "开始录音");
        recordingThread = new Thread(() -> {
            byte data[] = new byte[recordBufsize];
            File file = new File(FILE_NAME);
            FileOutputStream os = null;
            try {
                if (!file.exists()) {
                    file.createNewFile();
                    Log.i("audioRecordTest", "创建录音文件->" + FILE_NAME);
                }
                os = new FileOutputStream(file);//从头开始覆盖写入
            } catch (Exception e) {
                e.printStackTrace();
            }
            int read;
            if (os != null) {
                while (isRecording) {
                    read = audioRecord.read(data, 0, recordBufsize);
                    if (AudioRecord.ERROR_INVALID_OPERATION != read) {
                        try {
                            os.write(data);
                            Log.i("audioRecordTest", "写录音数据->" + read+FILE_NAME);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            try {
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        recordingThread.start();
    }

    private void stopRecord() {
        isRecording = false;
        if (audioRecord != null) {
            audioRecord.stop();
            Log.i("audioRecordTest", "停止录音");
            audioRecord.release();
            audioRecord = null;
            recordingThread = null;
        }
    }


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
//    private void initNews(String myType){
//        //从数据库中获取数据
////        new Thread(new Runnable() {
////            @Override
////            public void run() {
////                myNews = myDb.getNewsByType(myType);
////            }
////        }).start();
//        switch (myType){
//            case "推荐":
//                myNews.clear();
//                for(int i=0;i<20;i++){
//                    String[] content = {"苹果","梨子","香蕉"};
//                    News news_temp = new News("T"+i,"推荐新闻","新华社","2022-05-24",content,"推荐");
//                    myNews.add(news_temp);
//                }
//                break;
//            case "实时":
//                myNews.clear();
//                for(int i=0;i<20;i++){
//                    String[] content = {"苹果","梨子","香蕉"};
//                    News news_temp = new News("T"+i,"实时新闻","人民日报社","2022-05-24",content,"实时");
//                    myNews.add(news_temp);
//                }
//                break;
//            case "政治":
//                myNews.clear();
//                for(int i=0;i<20;i++){
//                    String[] content = {"苹果","梨子","香蕉"};
//                    News news_temp = new News("T"+i,"政治新闻","今日头条","2022-05-24",content,"政治");
//                    myNews.add(news_temp);
//                }
//                break;
//            case "军事":
//                myNews.clear();
//                for(int i=0;i<20;i++){
//                    String[] content = {"苹果","梨子","香蕉"};
//                    News news_temp = new News("T"+i,"军事新闻","封面新闻","2022-05-24",content,"军事");
//                    myNews.add(news_temp);
//                }
//                break;
//            case "娱乐":
//                myNews.clear();
//                for(int i=0;i<20;i++){
//                    String[] content = {"苹果","梨子","香蕉"};
//                    News news_temp = new News("T"+i,"娱乐新闻","青年日报","2022-05-24",content,"娱乐");
//                    myNews.add(news_temp);
//                }
//                break;
//            case "法律":
//                myNews.clear();
//                for(int i=0;i<20;i++){
//                    String[] content = {"苹果","梨子","香蕉"};
//                    News news_temp = new News("T"+i,"法律新闻","人民日报社","2022-05-24",content,"法律");
//                    myNews.add(news_temp);
//                }
//                break;
//            default:
//                break;
//        }
//    }
