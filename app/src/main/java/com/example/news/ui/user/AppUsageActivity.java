package com.example.news.ui.user;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.news.R;

public class AppUsageActivity extends BaseActivity {

    private String[] question_name_list = new String[]{"新闻主页布局","新闻选择小技巧",
            "新闻刷新小技巧","新闻阅读小技巧","新闻类型选择","新闻搜索小技巧"};
    private ListView questionList;
    private int[] icons={R.drawable.question};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_usage);

        questionList = findViewById(R.id.lv_question);
        questionList.setAdapter(new QuestionAdapter());
        questionList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 1:
                        AlertDialog.Builder builder_select_news = new AlertDialog.Builder(AppUsageActivity.this);
                        builder_select_news
                                .setMessage("为了方便用户阅读，新闻展示页一屏幕展示四条新闻,如果不喜欢当前四条，记得下拉刷新哦")
                                .setPositiveButton("好的", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //
                                    }
                                })
                                .show();
                        break;
                    case 5:
                        AlertDialog.Builder builder_dianziweilan_set = new AlertDialog.Builder(AppUsageActivity.this);
                        builder_dianziweilan_set
                                .setMessage("新闻搜索有语音输入和键盘输入两种方式，如果选用语音输入，请长按主页左下角按钮说话，说完后松开，就会自动识别您的语音啦！" +
                                        "若采用键盘输入，请直接点击主页上方的搜索栏进行输入，然后点击搜索栏右边的按钮进行搜索")
                                .setPositiveButton("好的", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //
                                    }
                                })
                                .show();
                        break;
                    case 2:
                        AlertDialog.Builder builder_theme_set = new AlertDialog.Builder(AppUsageActivity.this);
                        builder_theme_set
                                .setMessage("主页右边为新闻阅读区域，下拉即可进行刷新，每次刷新四条新闻，" +
                                        "四条新闻可以直接在屏幕中展示出来，不用下滑，读完之后就再刷新一次吧")
                                .setPositiveButton("好的", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //
                                    }
                                })
                                .show();
                        break;
                    case 3:
                        AlertDialog.Builder builder_theme_set2 = new AlertDialog.Builder(AppUsageActivity.this);
                        builder_theme_set2
                                .setMessage("听闻APP采用的是翻页式阅读方式，不是传统的下滑式设计，听完当前段落记得右滑翻页哦")
                                .setPositiveButton("好的", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //
                                    }
                                })
                                .show();
                        break;

                    case 0:
                        AlertDialog.Builder builder_theme = new AlertDialog.Builder(AppUsageActivity.this);
                        builder_theme
                                .setMessage("新闻主页分为三个板块，上方为搜索栏，左边边缘位置有垂直排列的两个按钮，左上按钮为使用指南入口，左下按钮为语音输入按钮，右边大区域为新闻阅读区域")
                                .setPositiveButton("好的", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //
                                    }
                                })
                                .show();
                        break;
                    case 4:
                        AlertDialog.Builder builder_type = new AlertDialog.Builder(AppUsageActivity.this);
                        builder_type
                                .setMessage("新闻类型又称为频道，新闻的默认分类为推荐、财经和股票，可通过语音输入”添加频道“" +
                                        "来进行添加，例如语音输入”添加娱乐“，即可添加娱乐频道,也可通过语音输入”删除频道“进行删除")
                                .setPositiveButton("好的", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //
                                    }
                                })
                                .show();
                        break;
                    default:
                        break;
                }
            }
        });



    }

    public void back_to_mine(View view) {
        finish();
    }

    class QuestionAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return question_name_list.length;
        }

        @Override
        public Object getItem(int position) {
            return question_name_list[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = LayoutInflater.from(AppUsageActivity.this).inflate(R.layout.activity_set_item,parent,false);
            TextView textView = (TextView)view.findViewById(R.id.mTvPeople);
            ImageView imageView = (ImageView)view.findViewById(R.id.people_image);
            textView.setText(question_name_list[position]);
            imageView.setBackgroundResource(icons[0]);
            return view;
        }
    }
}