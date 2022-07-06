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

    private String[] question_name_list = new String[]{"如何更改个人信息","如何设置电子围栏",
            "如何更改应用字体大小及样式","如何……"};
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
                    case 0:
                        AlertDialog.Builder builder_information_update = new AlertDialog.Builder(AppUsageActivity.this);
                        builder_information_update
                                .setMessage("进入【我的-个人资料编辑】页面，点击相应的信息可进行更改，记得点保存哟！")
                                .setPositiveButton("好的", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //
                                    }
                                })
                                .show();
                        break;
                    case 1:
                        AlertDialog.Builder builder_dianziweilan_set = new AlertDialog.Builder(AppUsageActivity.this);
                        builder_dianziweilan_set
                                .setMessage("进入【实时位置】页面，点击右下角红色警报图标，即可进入危险区域设置界面。")
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
                                .setMessage("进入【我的-App设置-主题设置】页面，即可设置应用的字体大小与样式，选择完毕后，记得点击下方的“一键应用主题”按钮哟。")
                                .setPositiveButton("好的", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //
                                    }
                                })
                                .show();
                        break;
                    case 3:

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