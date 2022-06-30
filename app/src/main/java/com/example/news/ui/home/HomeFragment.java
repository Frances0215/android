package com.example.news.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.example.news.DBUtils;
import com.example.news.R;
import com.example.news.TypeManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    private EditText mEtSearch;
    private TabLayout mTlNews;
    private ViewPager mVpNews;
    private DBUtils myDb ;
    private ImageView mIvAdd;
    private FloatingActionButton mFbRadio;
    //tab内容，这只是数据库中的type类型不包括推荐类型
    private ArrayList<String> myTypeTab = new ArrayList<>();
    private List<TabFragment> tabFragmentList = new ArrayList<>();
    private List<News> myNews;

    private TypeManager myTypeManager;

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
        mFbRadio = view.findViewById(R.id.mFbRadio);
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

        //语音输入
        mFbRadio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
