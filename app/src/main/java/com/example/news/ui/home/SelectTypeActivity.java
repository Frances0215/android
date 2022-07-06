package com.example.news.ui.home;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.news.MyDataBaseHelper;
import com.example.news.NewsAPP;
import com.example.news.NewsType;
import com.example.news.R;
import com.example.news.TypeManager;

import java.util.ArrayList;

public class SelectTypeActivity extends Activity {

    private TypeManager myTypeManager;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_type);

        //初始化数据库
        if(myTypeManager == null){
            myTypeManager = new TypeManager(this);
            myTypeManager.openDataBase();
        }
        //设置动态数目的按钮
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        RelativeLayout.LayoutParams layoutParamsroot = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT,RelativeLayout.LayoutParams.FILL_PARENT);
        RelativeLayout layoutroot = new RelativeLayout(this);
        layoutroot.setLayoutParams(layoutParamsroot);

        //得到一些全局变量,得到所有的type和我的type
        NewsAPP myAPP = (NewsAPP) getApplicationContext();
        String[] allType = myAPP.getNews_type();

        ArrayList<String> myType = myTypeManager.getAllMyType();

        NewsType op = new NewsType();
        op.outOtherType(allType,myType);
        ArrayList<String> otherType = op.getOtherType();
        RelativeLayout layout = new RelativeLayout(this);

        TextView mTvMyType = new TextView(this);
        mTvMyType.setText("我的频道");
        mTvMyType.setId(1001);
        mTvMyType.setTextSize(20);//单位是sp
        RelativeLayout.LayoutParams tvParams_m = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        tvParams_m.topMargin = 20;
        tvParams_m.leftMargin = 20;

        layout.addView(mTvMyType, tvParams_m);
        int j = -1;
        Button Btn[] = new Button[myType.size()];
        if(myType.size() >0) {

            for (int i = 0; i < myType.size(); i++) {
                Btn[i] = new Button(this);
                Btn[i].setId(i);
                String ButtonName = myType.get(i);
                Btn[i].setText(ButtonName);
                Btn[i].setTextSize(20);
                RelativeLayout.LayoutParams btParams = new RelativeLayout.LayoutParams((width - 70) / 2, 200);
                if (i % 2 == 0) {
                    j++;
                }
                btParams.leftMargin = 20 + ((width - 60) / 2 + 20) * (i % 2);   //横坐标定位
                btParams.topMargin = 110 + 205 * j;   //纵坐标定位
                layout.addView(Btn[i], btParams);   //将按钮放入layout组件
            }
        }
            int im_topmargin = 20 + 205 * (j + 1);//记录布局开始的纵坐标

            Button Btn2[] = new Button[otherType.size()];
            if (otherType.size() > 0) {

                TextView mTvOtherType = new TextView(this);
                mTvOtherType.setText("添加其他频道");
                mTvOtherType.setId(1002);
                mTvOtherType.setTextSize(20);//单位是sp
                RelativeLayout.LayoutParams tvParams_a = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                tvParams_a.topMargin = im_topmargin + 150;
                tvParams_a.leftMargin = 20;
                layout.addView(mTvOtherType, tvParams_a);
                int l = -1;
                for (int i = 0; i < otherType.size(); i++) {
                    Btn2[i] = new Button(this);
                    Btn2[i].setId(i + 3000);
                    String ButtonName = otherType.get(i);
                    Btn2[i].setText(ButtonName);
                    Btn2[i].setTextSize(20);
                    RelativeLayout.LayoutParams btParams2 = new RelativeLayout.LayoutParams((width - 70) / 2, 200);
                    if (i % 2 == 0) {
                        l++;
                    }
                    btParams2.leftMargin = 20 + ((width - 60) / 2 + 20) * (i % 2);   //横坐标定位
                    btParams2.topMargin = im_topmargin + 250 + 205 * l;   //纵坐标定位
                    layout.addView(Btn2[i], btParams2);   //将按钮放入layout组件
                }
            }

            //按钮布局在大布局中的位置，把上面的布局放到根布局中
            RelativeLayout.LayoutParams layoutParamsButton = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            layoutParamsButton.topMargin = 0;
            layoutParamsButton.leftMargin = 0;
            layoutroot.addView(layout, layoutParamsButton);

            this.setContentView(layoutroot);

                //批量设置监听
             if(myType.size()>0)
                for (int k = 0; k <= Btn.length - 1; k++) {
                    //这里不需要findId，因为创建的时候已经确定哪个按钮对应哪个Id
                    Btn[k].setTag(k);                //为按钮设置一个标记，来确认是按下了哪一个按钮
                    final int _k = k;

                    Btn[k].setOnClickListener(new Button.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int i = (Integer) v.getTag();   //这里的i不能在外部定义，因为内部类的关系，内部类好多繁琐的东西，要好好研究一番
                            boolean n = myTypeManager.deleteMyType((String) Btn[_k].getText());
                            //刷新只能在activity下不能用AppcomatActivity
                            onCreate(null);
                        }
                    });
                }


            if(otherType.size()>0)
                for (int n = 0; n <= Btn2.length - 1; n++) {
                    //这里不需要findId，因为创建的时候已经确定哪个按钮对应哪个Id
                    Btn2[n].setTag(n);                //为按钮设置一个标记，来确认是按下了哪一个按钮
                    final int _n = n;

                    Btn2[n].setOnClickListener(new Button.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int i = (Integer) v.getTag();   //这里的i不能在外部定义，因为内部类的关系，内部类好多繁琐的东西，要好好研究一番
                            myTypeManager.insertType((String) Btn2[_n].getText());
                            onCreate(null);
                        }
                    });
            }

//        //用于添加上方标题栏中的返回按钮
//        ActionBar actionBar = getSupportActionBar();
//        if (actionBar != null) {
//            actionBar.setHomeButtonEnabled(true);
//            actionBar.setDisplayHomeAsUpEnabled(true);
//        }


    }

    //返回上一个界面
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish(); // back button
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onResume() {
        if(myTypeManager == null){
            myTypeManager = new TypeManager(this);
            myTypeManager.openDataBase();
        }
        super.onResume();
    }

    @Override
    protected void onRestart() {
        if(myTypeManager != null){
            myTypeManager.closeDataBase();
            myTypeManager=null;
        }
        super.onRestart();
    }

    @Override
    protected void onPause() {
        if(myTypeManager != null){
            myTypeManager.closeDataBase();
            myTypeManager=null;
        }
        super.onPause();
    }
}
