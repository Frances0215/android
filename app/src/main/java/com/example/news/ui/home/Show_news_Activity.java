package com.example.news.ui.home;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.viewpager.widget.ViewPager;

import com.example.news.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class Show_news_Activity extends AppCompatActivity {

    private TabLayout myTab;
    private ViewPager mVpPager;
    private EditText mEtComment;
    private ImageView mIvComment;
    private ImageView mIvCollection;
    private List<PageFragment> pageFragmentList = new ArrayList<>();
    private News news_this;
    private int pageNumber = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_news);
        Intent intent=this.getIntent();
        Bundle bundle = intent.getExtras();
        news_this = (News)bundle.getSerializable("news");
        initView();
        //加一，用于放标题
        String[] Contents ={"奥法施工的活动公司发的方式告诉","dgfhfdggqrqtgwehdsgavdf","fasgqresfdfcas"};
        news_this.setContent_f(Contents);
        pageNumber = news_this.getContent_f().length +1;

        initTabViewpager();
        initTab();
        myTab.setupWithViewPager(mVpPager,false);

        //用于添加上方标题栏中的返回按钮1
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

    }

    //用于添加上方标题栏中的返回按钮2
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish(); // back button
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initView() {
        myTab=(TabLayout) findViewById(R.id.mTbPage);
        mVpPager=(ViewPager) findViewById(R.id.mVgPage);
        mEtComment=(EditText) findViewById(R.id.mEtComment);
        mIvCollection=(ImageView) findViewById(R.id.mIvCollection);
        mIvComment=(ImageView) findViewById(R.id.mIvComment);
    }

    private void initTabViewpager(){
        //添加适配器
        mVpPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                return pageFragmentList.get(position);
            }

            @Override
            public int getCount() {
                return pageFragmentList.size();
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                int num = position+1;
                return "第"+num+"页";
            }
        });

    }

    private void initTab(){
        //添加tab
        for (int i = 0; i < pageNumber; i++) {
            TabLayout.Tab tab = myTab.newTab();
            int num = i+1;
            myTab.addTab(tab.setText("第"+num+"页"));
            pageFragmentList.add(PageFragment.newInstance(i,news_this));
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        mVpPager.getAdapter().notifyDataSetChanged();
    }
}